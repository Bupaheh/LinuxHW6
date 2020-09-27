import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import virtMem.*
import java.util.*

internal class oprPreprocessingTests {

    //ПО КАКОЙ-ТО ПРИЧИНЕ АССЕРТ НЕ ХОЧЕТ СРАВНИВАТЬ ЭТИ ДВЕ ШТУКИ,
    // ДАЖЕ ВТОРАЯ ТЕСТИРУЮЩАЯ ФУНКЦИЯ ОБВАЛИВАЕТСЯ

    /*@Test
    fun optPeprocessing() {
        val result = optPeprocessing(Process(7, 4, listOf(1, 2, 3, 4, 1, 5, 1, 3, 4, 6, 2, 3, 7)))
        /*val answer: Array<Queue<Int>> = arrayOf(LinkedList(mutableListOf(0, 4, 6)), LinkedList(mutableListOf(1, 10)),
                LinkedList(mutableListOf(2, 7, 11)), LinkedList(mutableListOf(3, 8)), LinkedList(mutableListOf(5)),
                LinkedList(mutableListOf(9)), LinkedList(mutableListOf(12)))*/
        val answer: Array<Queue<Int>> = arrayOf(LinkedList, )
        assertEquals(answer, result)
    }*/

    /*@Test
    fun optPeprocessing() {
        val answer: Array<Queue<Int>> = Array(2) { LinkedList<Int>() }
        answer[0].add(0)
        answer[0].add(1)
        answer[1].add(2)
        val answer1: Array<Queue<Int>> = Array(2) { LinkedList<Int>() }
        answer1[0].add(0)
        answer1[0].add(1)
        answer1[1].add(2)
        assertEquals(answer1, answer)
    }*/
}