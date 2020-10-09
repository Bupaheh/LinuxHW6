package virtMem
import java.io.BufferedWriter
import java.io.File
import kotlin.random.Random

data class Process(var processDataSize: Int = -1, var ramSize: Int = -1, var listOfRequests: List<Int> = listOf())

typealias InputData = List<List<String>>

//Выводит строку в outputFile.
fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

//В каждый элемент inputData записывается список, относящийся к определённому
//запросу во входных данных (каждый разпос разделен символом '-').
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

//Генерирует тест.
fun generateTest(args: Array <String>, outputFile: BufferedWriter) {
    val processDataSize = args[1].toIntOrNull() ?: -1
    val ramSize = args[2].toIntOrNull() ?: -1
    val numberOfRequests = args[3].toIntOrNull() ?: -1
    if(processDataSize < 1 || ramSize < 1 || numberOfRequests < 1)
        output("Incorrect generation data", outputFile)
    else {
        output(args[1] + " " + args[2], outputFile)
        output(IntArray(numberOfRequests) { Random.nextInt(1, processDataSize + 1) }.joinToString(" "), outputFile)
    }
    outputFile.close()
}

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
    if(args.size > 3 && args[0] == "gen") {
        generateTest(args, File("outputTest.txt").bufferedWriter())
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