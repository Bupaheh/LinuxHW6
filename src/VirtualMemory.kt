package virtMem
import java.io.BufferedWriter
import java.io.File

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

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    val inputData: ArrayList <ArrayList<String>> = arrayListOf()
    if(args.isEmpty() && inputProcessing(args, inputData))
        output("Incorrect input data", outputFile)
    else {

    }
}