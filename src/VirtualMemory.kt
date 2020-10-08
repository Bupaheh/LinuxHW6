package virtMem
import java.io.BufferedWriter
import java.io.File
import java.util.*
import kotlin.random.Random

data class Process(var processDataSize: Int = -1, var ramSize: Int = -1, var listOfRequests: List<Int> = listOf())

typealias InputData = List<List<String>>
typealias LastRequestQueue = SortedSet<Pair<Int, Int>>
typealias FutureRequestsQueue = PriorityQueue<Pair<Int, Int>>
typealias FutureRequestsOfEachPage = Array<Queue<Int>>

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

//Реализует FIFO (first in — first out).
//result list - список замещаемых страниц.
//numberOfChanges - количество замещений кадров.
//positionInRam - positionInRam[page] - номер кадра, в котором находится страница page.
//firstIn - очередь, где первый элемент - страница,
//которая попала в оперативную память раньше всех остальных.

//Если запрашиваемая страница не присутствует в оперативной памяти,
//то запускается fifoNotInRamIteration().
fun fifo(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val positionInRam = IntArray(process.processDataSize) {-1}
    val firstIn: Queue<Int> = LinkedList()

    for(request in process.listOfRequests) {
        if(positionInRam[request - 1] != -1)
            resultList.add(0)
        else {
            fifoNotInRamIteration(process, request, firstIn, positionInRam, resultList, numberOfChanges)
            numberOfChanges++
        }
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

//Реализация случая, когда запрашиваемая страница не находится в оперативной памяти.
//Если в памяти есть свободное место, то страница записывается туда.
//В обратном случае замещается страница, которая попала в оперативную память раньше всех остальных.
fun fifoNotInRamIteration(process: Process, request: Int, firstIn: Queue<Int>,
                          positionInRam: IntArray, resultList: MutableList<Int>, numberOfChanges: Int) {
    val position = if(numberOfChanges < process.ramSize) //there is empty slot in ram
        numberOfChanges + 1
    else {
        val first = firstIn.poll()
        val temp = positionInRam[first - 1]
        positionInRam[first - 1] = -1
        temp
    }
    firstIn.add(request) //add new element in queue
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}

//LRU (least recently used).
//result list - список замещаемых страниц.
//numberOfChanges - количество замещений кадров.
//positionInRam - positionInRam[page] - номер кадра, в котором находится страница page.
//lastRequestQueue - очередь с приоритетом, в которую добавляются элементы Pair(page, time)
//при обращении к странице page. Элементы сортируются по возрастанию time.
//lastRequest - lastRequest[page] - время последнего обращения к странице page. Нужен, чтобы
//удалять устаревшие элементы из lastRequestQueue.

//Удаляем из lastRequestQueue предыдущий запрос к этой странице.
//Добавляем текущий запрос в lastRequestQueue
//Если запрашиваемая страница не присутствует в оперативной памяти,
//то запускается lruNotInRamIteration().
fun lru(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val positionInRam = IntArray(process.processDataSize) {-1}
    val lastRequestQueue: LastRequestQueue = sortedSetOf(compareBy{it.second}) //first element in pair - page number, second - time
    val lastRequest = IntArray(process.processDataSize) {-1}

    for((time, request) in process.listOfRequests.withIndex()) {
        lastRequestQueue.remove(Pair(request, lastRequest[request - 1])) //remove outdated element
        lastRequest[request - 1] = time //update time of last use
        lastRequestQueue.add(Pair(request, time))
        if(positionInRam[request - 1] != -1) {
            resultList.add(0)
            continue
        }
        lruNotInRamIteration(process, request, lastRequestQueue, positionInRam, resultList, numberOfChanges)
        numberOfChanges++
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

//Реализация случая, когда запрашиваемая страница не находится в оперативной памяти.
//Если в памяти есть свободное место, то страница записывается туда.
//В обратном случае замещается страница, к которой дольше всего не было обращений.
fun lruNotInRamIteration(process: Process, request: Int, lastRequestQueue: LastRequestQueue,
                         positionInRam: IntArray, resultList: MutableList<Int>, numberOfChanges: Int) {
    val position = if(numberOfChanges < process.ramSize) //there is empty slot in ram
        numberOfChanges + 1
    else {
        val leastRecentlyUsed = lastRequestQueue.first().first
        lastRequestQueue.remove(lastRequestQueue.first())
        val temp = positionInRam[leastRecentlyUsed - 1]
        positionInRam[leastRecentlyUsed - 1] = -1
        temp
    }
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}

//OPT (optimal).
//result list - список замещаемых страниц.
//numberOfChanges - количество замещений кадров.
//positionInRam - positionInRam[page] - номер кадра, в котором находится страница page.
//futureRequests - futureRequests[page] - очередь обращений к странице page.
//futureRequestsQueue - очередь с приоритетом, в которую добавляются элементы Pair(page, time)
//при обращении к странице page. time - время следующего запроса к данной странице.
//Элементы сортируются по убыванию time.

//Удаляем текущий запрос из futureRequests[page - 1].
//Если запрашиваемая страница находится в оперативной памяти,
//то добавляем следующий запрос к этой странице в futureRequestsQueue.
//Если запрашиваемая страница не присутствует в оперативной памяти,
//то запускается optNotInRamIteration().
fun opt(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val positionInRam = IntArray(process.processDataSize) {-1}
    val futureRequests = optPreprocessing(process) //queue of future requests of each page
    val futureRequestsQueue = FutureRequestsQueue(compareBy{ -it.second }) //first element in pair - page number, second - time

    for(request in process.listOfRequests) {
        futureRequests[request - 1].poll() //delete future request that is happening now
        if(positionInRam[request - 1] != -1) {
            resultList.add(0)
            futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
            continue
        }
        optNotInRamIteration(process, request, futureRequests, futureRequestsQueue, positionInRam, resultList, numberOfChanges)
        numberOfChanges++
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

//Препроцессинг запросов к страницам процесса.
//result - result[page] - очередь обращений к странице page.
//infinity - значение, необходимое для обозначения того, что к странице больше не будет запросов.
fun optPreprocessing(process: Process): FutureRequestsOfEachPage {
    val result: FutureRequestsOfEachPage = Array(process.processDataSize) { LinkedList<Int>() }
    val infinity = process.listOfRequests.size
    process.listOfRequests.forEachIndexed {time, element -> result[element - 1].add(time)}
    result.forEach { element -> element.add(infinity) }
    return result
}

//Реализация случая, когда запрашиваемая страница не находится в оперативной памяти.
//Если в памяти есть свободное место, то страница записывается туда.
//В обратном случае замещается страница, к которой дольше всего не будет обращений.
//Добавляем следующий запрос к этой странице в futureRequestsQueue.
fun optNotInRamIteration(process: Process, request: Int, futureRequests: FutureRequestsOfEachPage, futureRequestsQueue: FutureRequestsQueue,
                         positionInRam: IntArray, resultList: MutableList<Int>, numberOfChanges: Int) {
    val position = if(numberOfChanges < process.ramSize) //there is empty slot in ram
        numberOfChanges + 1
    else {
        val optimalPage = futureRequestsQueue.poll().first
        val temp = positionInRam[optimalPage - 1]
        positionInRam[optimalPage - 1] = -1
        temp
    }
    futureRequestsQueue.add(Pair(request, futureRequests[request - 1].peek())) //add new future request in priorityQueue
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
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