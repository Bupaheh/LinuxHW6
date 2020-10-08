package virtMem

import java.util.*

typealias FutureRequestsQueue = PriorityQueue<Pair<Int, Int>>
typealias FutureRequestsOfEachPage = Array<Queue<Int>>

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
    val futureRequestOfThePage = optPreprocessing(process) //queue of future requests of each page
    val futureRequestsQueue = FutureRequestsQueue(compareBy{ -it.second }) //first element in pair - page number, second - time

    for(request in process.listOfRequests) {
        futureRequestOfThePage[request - 1].poll() //delete future request that is happening now
        if(positionInRam[request - 1] != -1) { //page currently in ram
            resultList.add(0)
            futureRequestsQueue.add(Pair(request, futureRequestOfThePage[request - 1].peek())) //add new future request in priorityQueue
            continue
        }
        optNotInRamIteration(process, request, futureRequestOfThePage, futureRequestsQueue, positionInRam, resultList, numberOfChanges)
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
fun optNotInRamIteration(process: Process, request: Int, futureRequestOfThePage: FutureRequestsOfEachPage, futureRequestsQueue: FutureRequestsQueue,
                         positionInRam: IntArray, resultList: MutableList<Int>, numberOfChanges: Int) {
    val position = if(numberOfChanges < process.ramSize) //there is empty slot in ram
        numberOfChanges + 1
    else {
        val optimalPage = futureRequestsQueue.poll().first
        val temp = positionInRam[optimalPage - 1]
        positionInRam[optimalPage - 1] = -1 //remove page from ram
        temp
    }
    futureRequestsQueue.add(Pair(request, futureRequestOfThePage[request - 1].peek())) //add new future request in priorityQueue
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}