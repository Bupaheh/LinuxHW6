package virtMem
import java.io.BufferedWriter
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class Process(var processDataSize: Int = -1, var ramSize: Int = -1, var listOfRequests: List<Int> = listOf())

fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

fun inputProcessing(args: Array <String>, inputData: ArrayList <ArrayList<String>>):Boolean {
    val input = File(args[0])
    if(!input.exists())
        return false
    val inputInformation = input.readLines()
    var index = 0
    inputData.add(arrayListOf())
    inputInformation.map { element ->
        if(element == "-") {
            index++
            inputData.add(arrayListOf())
        }
        else
            inputData[index].add(element)
    }
    return true
}

fun toProcess(inputList: ArrayList<String>): Pair<Process, Boolean> {
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

fun algoStart(inputData: ArrayList <ArrayList<String>>, outputFile: BufferedWriter) {
    for (inputRequests in inputData) {
        val processPair = toProcess(inputRequests)
        if(processPair.second) {
            val process = processPair.first
            algoIteration(process)
        }
        else
            output("Incorrect input data", outputFile)
    }
}

fun algoIteration(process: Process) {
    val fifoResult = fifo(process)
    val lruResult = lru(process)
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

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    val inputData: ArrayList <ArrayList<String>> = arrayListOf()
    if(args.isEmpty() && inputProcessing(args, inputData))
        output("Incorrect input data", outputFile)
    else {
        algoStart(inputData, outputFile)
    }
}