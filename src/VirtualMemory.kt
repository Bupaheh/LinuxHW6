package virtMem
import java.io.BufferedWriter
import java.io.File
import java.util.*

data class Process(var processDataSize: Int = -1, var ramSize: Int = -1, var listOfRequests: List<Int> = listOf())

typealias InputData = List<List<String>>

fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

fun inputProcessing(args: Array <String>): Pair<InputData, Boolean> {
    val input = File(args[0])
    if(!input.exists())
        return Pair(listOf(listOf<String>()), false)
    val inputInformation = input.readLines()
    val inputData: InputData = inputInformation.fold(mutableListOf(mutableListOf<String>())) { list, element ->
        if(element == "-")
            list.add(mutableListOf())
        else
            list.last().add(element)
        list
    }
    return Pair(inputData, true)
}

fun toProcess(inputList: List<String>): Pair<Process, Boolean> {
    val resultProcess = Process()
    if(inputList.size != 2)
        return Pair(resultProcess, false)
    val sizes = inputList[0].split(" ")
    val requests = inputList[1].split(" ")
    if(!isCorrect(sizes, requests))
        return Pair(resultProcess, false)
    resultProcess.processDataSize = sizes.first().toInt()
    resultProcess.ramSize = sizes.last().toInt()
    resultProcess.listOfRequests = requests.map{ element -> element.toInt()}
    return Pair(resultProcess, true)
}

fun isCorrect(sizes: List<String>, requests: List<String>): Boolean {
    return sizes.size == 2 &&
            sizes.all { element -> (element.toIntOrNull() != null) && (element.toInt() > 0)} &&
            requests.all { element -> (element.toIntOrNull() != null) && (element.toInt() > 0)}
}

fun algoStart(inputData: InputData, outputFile: BufferedWriter) {
    for (inputRequests in inputData) {
        val (process, toProcessBoolean) = toProcess(inputRequests)
        if(toProcessBoolean)
            algoIteration(process, outputFile)
        else
            output("Incorrect input data", outputFile)
    }
}

fun algoIteration(process: Process, outputFile: BufferedWriter) {
    val fifoResult = fifo(process)
    val lruResult = lru(process)
    val optResult = opt(process)

    output("fifo: " + fifoResult.first, outputFile)
    output("lru: " + lruResult.first, outputFile)
    output("opt: " + optResult.first, outputFile)
    output("fifoCount: " + fifoResult.second, outputFile)
    output("lruCount: " + lruResult.second, outputFile)
    output("optCount: " + optResult.second, outputFile)
    output("-", outputFile)
}

fun fifo(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val nowInRam = mutableSetOf<Int>()
    val firstIn: Queue<Int> = LinkedList()
    val positionInRam = IntArray(process.processDataSize)

    for(request in process.listOfRequests) {
        if(request in nowInRam) {
            resultList.add(0)
            continue
        }
        numberOfChanges++
        var position: Int
        if(nowInRam.size < process.ramSize) //there is empty slot in ram
            position = nowInRam.size + 1
        else {
            val first = firstIn.poll()
            position = positionInRam[first - 1]
            nowInRam.remove(first)
        }
        firstIn.add(request)
        positionInRam[request - 1] = position
        resultList.add(position)
        nowInRam.add(request)
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun lru(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val nowInRam = mutableSetOf<Int>()
    val lastRequestQueue = PriorityQueue<Pair<Int, Int>>(compareBy{it.second}) //first element in pair - page number, second - time
    val lastRequest = IntArray(process.processDataSize)
    val positionInRam = IntArray(process.processDataSize)

    for((time, request) in process.listOfRequests.withIndex()) {
        lastRequest[request - 1] = time //update time of last use
        lastRequestQueue.add(Pair(request, time))
        if(request in nowInRam) {
            resultList.add(0)
            continue
        }
        numberOfChanges++
        var position: Int
        if(nowInRam.size < process.ramSize) //there is empty slot in ram
            position = nowInRam.size + 1
        else {
            while(!((lastRequest[lastRequestQueue.peek().first - 1] == lastRequestQueue.peek().second) &&
                    (lastRequestQueue.peek().first in nowInRam))) //outdated elements
                lastRequestQueue.poll()
            val leastRecentlyUsed = lastRequestQueue.poll().first
            position = positionInRam[leastRecentlyUsed - 1]
            nowInRam.remove(leastRecentlyUsed)
        }
        positionInRam[request - 1] = position
        resultList.add(position)
        nowInRam.add(request)
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun opt(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val nowInRam = mutableSetOf<Int>()
    val futureRequests = optPreprocessing(process) //queue of future requests of each page
    val futureRequestsQueue = PriorityQueue<Pair<Int, Int>>(compareBy{ -it.second }) //first element in pair - page number, second - time
    val positionInRam = IntArray(process.processDataSize)

    for(request in process.listOfRequests) {
        futureRequests[request - 1].poll() //delete future request that is happening now
        if(request in nowInRam) {
            resultList.add(0)
            futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
            continue
        }
        numberOfChanges++
        var position: Int
        if(nowInRam.size < process.ramSize) //there is empty slot in ram
            position = nowInRam.size + 1
        else {
            while(!((futureRequestsQueue.peek().second ==
                    futureRequests[futureRequestsQueue.peek().first - 1].peek()) &&
                    (futureRequestsQueue.peek().first in nowInRam))) //outdated elements
                futureRequestsQueue.poll()
            val optimalPage = futureRequestsQueue.poll().first
            position = positionInRam[optimalPage - 1]
            nowInRam.remove(optimalPage)
        }
        futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
        positionInRam[request - 1] = position
        resultList.add(position)
        nowInRam.add(request)
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun optPreprocessing(process: Process): Array<Queue<Int>> {
    val result: Array<Queue<Int>> = Array(process.processDataSize) { LinkedList<Int>() }
    process.listOfRequests.forEachIndexed {time, element -> result[element - 1].add(time)}
    val infinity = 1e9.toInt()
    result.forEach { element -> element.add(infinity) }
    return result
}

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    val (inputData, isCorrect) = inputProcessing(args)
    if(args.isEmpty() || !isCorrect)
        output("Incorrect input data", outputFile)
    else {
        algoStart(inputData, outputFile)
    }
    outputFile.close()
}