package virtMem

import java.util.*

typealias LastRequestQueue = SortedSet<Pair<Int, Int>>

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
        lastRequestQueue.add(Pair(request, time)) //add new element
        if(positionInRam[request - 1] != -1) { //page currently in ram
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
        val leastRecentlyUsed = lastRequestQueue.first().first //get page that was least recently used
        lastRequestQueue.remove(lastRequestQueue.first())
        val temp = positionInRam[leastRecentlyUsed - 1]
        positionInRam[leastRecentlyUsed - 1] = -1 //remove page from ram
        temp
    }
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}