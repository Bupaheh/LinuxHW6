package virtMem
import java.io.BufferedWriter
import java.io.File

data class Process(val processDataSize: Int, val ramSize: Int, val listOfRequests: List<Int>)

fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

fun inputProcessing(args: Array <String>, inputData: MutableList <MutableList<String>>):Boolean {
    val input = File(args[0])
    if(!input.exists())
        return false
    val inputInformation = input.readLines()
    var index = 0
    inputData.add(mutableListOf())
    inputInformation.map { element ->
        if(element == "-") {
            index++
            inputData.add(mutableListOf())
        }
        else
            inputData[index].add(element)
    }
    return true
}

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    val inputData: MutableList <MutableList<String>> = mutableListOf()
    if(args.isEmpty() && inputProcessing(args, inputData))
        output("Incorrect input data", outputFile)
    else {

    }
}