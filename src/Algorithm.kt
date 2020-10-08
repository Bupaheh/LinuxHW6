package virtMem

import java.io.BufferedWriter

//Запускает пакетную обработку.
fun algoStart(inputData: InputData, outputFile: BufferedWriter) {
    for (inputRequests in inputData) {
        val (process, toProcessBoolean) = toProcess(inputRequests)
        if(toProcessBoolean)
            algoIteration(process, outputFile)
        else
            output("Incorrect input data", outputFile)
    }
}

//Обработка одного запроса входных данных.
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

//Преобразует список в Process.
//В списке должно содержаться два элемента (в первом элементе -
//два числа (processDataSize и ramSize), во втором элементе -
//список запросов к страницам процесса.
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

//Проверяет корректность двух списков.
fun isCorrect(sizes: List<String>, requests: List<String>): Boolean {
    return sizes.size == 2 && //there must be two elements in sizes
            sizes.all { element -> (element.toIntOrNull() != null) && (element.toInt() > 0)} && //each number must be positive
            requests.all { element -> (element.toIntOrNull() != null) && (element.toInt() > 0)}
}