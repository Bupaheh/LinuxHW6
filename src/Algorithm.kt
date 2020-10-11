package virtMem

import java.io.BufferedWriter

enum class ToProcessErrors {
    Correct,
    IncorrectNumberOfRows,
    NotPositiveNumberOnTheFirstLine,
    NotPositiveNumberOnTheSecondLine,
    IncorrectNumberOfVariablesOnTheFirstLine
}

//Запускает пакетную обработку.
fun algoBegin(inputData: InputData, outputFile: BufferedWriter) {
    for (inputRequests in inputData) {
        val (process, toProcessResult) = toProcess(inputRequests)
        when(toProcessResult) {
            ToProcessErrors.IncorrectNumberOfRows ->
                output("Error: Incorrect number of rows", outputFile)
            ToProcessErrors.NotPositiveNumberOnTheFirstLine ->
                output("Error: Not positive number on the first line", outputFile)
            ToProcessErrors.NotPositiveNumberOnTheSecondLine ->
                output("Error: Not positive number on the second line", outputFile)
            ToProcessErrors.IncorrectNumberOfVariablesOnTheFirstLine ->
                output("Error: Incorrect number of variables on the first line", outputFile)
            ToProcessErrors.Correct ->
                    algoIteration(process, outputFile)
        }
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
fun toProcess(inputList: List<String>): Pair<Process, ToProcessErrors> {
    if(inputList.size != 2)
        return Pair(Process(), ToProcessErrors.IncorrectNumberOfRows) //in input data must be only two rows
    val sizes = inputList[0].split(" ")
    val requests = inputList[1].split(" ")
    val isCorrectResult = isCorrect(sizes, requests)
    if(isCorrectResult != ToProcessErrors.Correct)
        return Pair(Process(), isCorrectResult)
    val resultProcess = Process(sizes.first().toInt(), sizes.last().toInt(), requests.map{ element -> element.toInt()})
    return Pair(resultProcess, ToProcessErrors.Correct)
}

//Проверяет корректность двух списков.
fun isCorrect(sizes: List<String>, requests: List<String>): ToProcessErrors {
    if(sizes.any { element -> (element.toIntOrNull() == null) || (element.toInt() <= 0)})
        return ToProcessErrors.NotPositiveNumberOnTheFirstLine
    if(sizes.size != 2)
        return ToProcessErrors.IncorrectNumberOfVariablesOnTheFirstLine
    if(requests.any { element -> (element.toIntOrNull() == null) || (element.toInt() <= 0)})
        return ToProcessErrors.NotPositiveNumberOnTheSecondLine
    return ToProcessErrors.Correct
}