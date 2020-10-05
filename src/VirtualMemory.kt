package virtMem
import java.io.BufferedWriter
import java.io.File
import java.util.*
import kotlin.random.Random

data class Process(var processDataSize: Int = -1, var ramSize: Int = -1, var listOfRequests: List<Int> = listOf())

typealias InputData = List<List<String>>
typealias LastRequestQueue = PriorityQueue<Pair<Int, Int>>
typealias FutureRequestsQueue = PriorityQueue<Pair<Int, Int>>
typealias FutureRequestsOfEachPage = Array<Queue<Int>>

fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

fun inputProcessing(args: Array <String>): Pair<InputData, Boolean> {
    val input = File(args[0])
    if(!input.exists())
        return Pair(listOf(listOf()), false)
    val inputInformation = input.readLines()
    val inputData: InputData = inputInformation.fold(mutableListOf(mutableListOf<String>())) { list, element ->
        if(element == "-")              //input requests separated by '-'
            list.add(mutableListOf())
        else
            list.last().add(element)
        list
    }
    return Pair(inputData, true)
}

fun toProcess(inputList: List<String>): Pair<Process, Boolean> {
    if(inputList.size != 2)
        return Pair(Process(), false) //in input data must be only two rows
    val sizes = inputList[0].split(" ")
    val requests = inputList[1].split(" ")
    if(!isCorrect(sizes, requests))
        return Pair(Process(), false)
    val resultProcess = Process(sizes.first().toInt(), sizes.last().toInt(), requests.map{ element -> element.toInt()})
    return Pair(resultProcess, true)
}

fun isCorrect(sizes: List<String>, requests: List<String>): Boolean {
    return sizes.size == 2 && //there must be two elements in sizes
            sizes.all { element -> (element.toIntOrNull() != null) && (element.toInt() > 0)} && //each number must be positive
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
        if(request in nowInRam)
            resultList.add(0)
        else {
            numberOfChanges++
            fifoNotInRamIteration(process, request, nowInRam, firstIn, positionInRam, resultList)
        }
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun fifoNotInRamIteration(process: Process, request: Int, nowInRam: MutableSet<Int>, firstIn: Queue<Int>,
                          positionInRam: IntArray, resultList: MutableList<Int>) {
    val position = if(nowInRam.size < process.ramSize) //there is empty slot in ram
        nowInRam.size + 1
    else {
        val first = firstIn.poll()
        nowInRam.remove(first)
        positionInRam[first - 1]
    }
    firstIn.add(request) //add new element in queue
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
    nowInRam.add(request)
}

fun lru(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val nowInRam = mutableSetOf<Int>()
    val lastRequestQueue = LastRequestQueue(compareBy{it.second}) //first element in pair - page number, second - time
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
        lruNotInRamIteration(process, request, nowInRam, lastRequestQueue, lastRequest, positionInRam, resultList)
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun lruNotInRamIteration(process: Process, request: Int, nowInRam: MutableSet<Int>, lastRequestQueue: LastRequestQueue,
                         lastRequest: IntArray, positionInRam: IntArray, resultList: MutableList<Int>) {
    val position = if(nowInRam.size < process.ramSize) //there is empty slot in ram
        nowInRam.size + 1
    else {
        while(!((lastRequest[lastRequestQueue.peek().first - 1] == lastRequestQueue.peek().second) &&
                        (lastRequestQueue.peek().first in nowInRam))) //outdated elements
            lastRequestQueue.poll()
        val leastRecentlyUsed = lastRequestQueue.poll().first
        nowInRam.remove(leastRecentlyUsed)
        positionInRam[leastRecentlyUsed - 1]
    }
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
    nowInRam.add(request)
}

fun opt(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val nowInRam = mutableSetOf<Int>()
    val futureRequests = optPreprocessing(process) //queue of future requests of each page
    val futureRequestsQueue = FutureRequestsQueue(compareBy{ -it.second }) //first element in pair - page number, second - time
    val positionInRam = IntArray(process.processDataSize)

    for(request in process.listOfRequests) {
        futureRequests[request - 1].poll() //delete future request that is happening now
        if(request in nowInRam) {
            resultList.add(0)
            futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
            continue
        }
        numberOfChanges++
        optNotInRamIteration(process, request, nowInRam, futureRequests, futureRequestsQueue, positionInRam, resultList)
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

fun optPreprocessing(process: Process): FutureRequestsOfEachPage {
    val result: Array<Queue<Int>> = Array(process.processDataSize) { LinkedList<Int>() }
    process.listOfRequests.forEachIndexed {time, element -> result[element - 1].add(time)}
    val infinity = 1e9.toInt()
    result.forEach { element -> element.add(infinity) }
    return result
}

fun optNotInRamIteration(process: Process, request: Int, nowInRam: MutableSet<Int>, futureRequests: FutureRequestsOfEachPage,
                         futureRequestsQueue: FutureRequestsQueue, positionInRam: IntArray, resultList: MutableList<Int>) {
    val position = if(nowInRam.size < process.ramSize) //there is empty slot in ram
        nowInRam.size + 1
    else {
        while(!((futureRequestsQueue.peek().second ==
                        futureRequests[futureRequestsQueue.peek().first - 1].peek()) &&
                        (futureRequestsQueue.peek().first in nowInRam))) //outdated elements
            futureRequestsQueue.poll()
        val optimalPage = futureRequestsQueue.poll().first
        nowInRam.remove(optimalPage)
        positionInRam[optimalPage - 1]
    }
    futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
    positionInRam[request - 1] = position //update position in ramK
    resultList.add(position)
    nowInRam.add(request)
}

fun generateTest(args: Array <String>, outputFile: BufferedWriter) {
    val processDataSize = args[1].toIntOrNull() ?: -1
    val ramSize = args[2].toIntOrNull() ?: -1
    val numberOfRequests = args[3].toIntOrNull() ?: -1
    if(processDataSize < 1 || ramSize < 1 || numberOfRequests < 1)
        output("Incorrect input data", outputFile)
    else {
        output(args[1] + " " + args[2], outputFile)
        output(IntArray(numberOfRequests) { Random.nextInt(1, processDataSize + 1) }.joinToString(" "), outputFile)
    }
}

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    if(args.size > 3 && args[0] == "gen") {
        generateTest(args, outputFile)
    }
    else {
        val (inputData, isCorrect) = inputProcessing(args)
        if (args.isEmpty() || !isCorrect)
            output("File doesn't exist", outputFile)
        else {
            algoStart(inputData, outputFile)
        }
    }
    outputFile.close()
}