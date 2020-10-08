package virtMem

import java.util.*

typealias LeastRecentlyUsedQueue = SortedSet<Pair<Int, Int>>

//LRU (least recently used).
//result list - список замещаемых страниц.
//numberOfChanges - количество замещений кадров.
//positionInRam - positionInRam[page] - номер кадра, в котором находится страница page.
//leastRecentlyUsedQueue - очередь с приоритетом, в которую добавляются элементы Pair(page, time)
//при обращении к странице page. Элементы сортируются по возрастанию time.
//lastRequestOfThePage - lastRequestOfThePage[page] - время последнего обращения к странице page.
//Нужен, чтобы удалять устаревшие элементы из leastRecentlyUsedQueue.

//Удаляем из leastRecentlyUsedQueue предыдущий запрос к этой странице.
//Добавляем текущий запрос в leastRecentlyUsedQueue
//Если запрашиваемая страница не присутствует в оперативной памяти,
//то запускается lruNotInRamIteration().
fun lru(process: Process): Pair<String, Int> {
    val resultList = mutableListOf<Int>()
    var numberOfChanges = 0
    val positionInRam = IntArray(process.processDataSize) {-1}
    val leastRecentlyUsedQueue: LeastRecentlyUsedQueue = sortedSetOf(compareBy{it.second}) //first element in pair - page number, second - time
    val lastRequestOfThePage = IntArray(process.processDataSize) {-1}

    for((time, request) in process.listOfRequests.withIndex()) {
        leastRecentlyUsedQueue.remove(Pair(request, lastRequestOfThePage[request - 1])) //remove outdated element
        lastRequestOfThePage[request - 1] = time //update time of last use
        leastRecentlyUsedQueue.add(Pair(request, time)) //add new element
        if(positionInRam[request - 1] != -1) { //page currently in ram
            resultList.add(0)
            continue
        }
        lruNotInRamIteration(process, request, leastRecentlyUsedQueue, positionInRam, resultList, numberOfChanges)
        numberOfChanges++
    }
    return Pair(resultList.joinToString(" "), numberOfChanges)
}

//Реализация случая, когда запрашиваемая страница не находится в оперативной памяти.
//Если в памяти есть свободное место, то страница записывается туда.
//В обратном случае замещается страница, к которой дольше всего не было обращений.
fun lruNotInRamIteration(process: Process, request: Int, leastRecentlyUsedQueue: LeastRecentlyUsedQueue,
                         positionInRam: IntArray, resultList: MutableList<Int>, numberOfChanges: Int) {
    val position = if(numberOfChanges < process.ramSize) //there is empty slot in ram
        numberOfChanges + 1
    else {
        val leastRecentlyUsed = leastRecentlyUsedQueue.first().first //get page that was least recently used
        leastRecentlyUsedQueue.remove(leastRecentlyUsedQueue.first())
        val temp = positionInRam[leastRecentlyUsed - 1]
        positionInRam[leastRecentlyUsed - 1] = -1 //remove page from ram
        temp
    }
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}