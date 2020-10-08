package virtMem

import java.util.*

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
        if(positionInRam[request - 1] != -1) //page currently in ram
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
        positionInRam[first - 1] = -1 //remove page from ram
        temp
    }
    firstIn.add(request) //add new element in queue
    positionInRam[request - 1] = position //update position in ram
    resultList.add(position)
}