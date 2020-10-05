import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import virtMem.*
import java.util.*

internal class OptPreprocessingTests {

    private val infinity = 1e9.toInt()

    @Test
    fun `optPreprocessing test`() {
        val result = optPreprocessing(Process(7, 4, listOf(1, 2, 3, 4, 1, 5, 1, 3, 4, 6, 2, 3, 7)))
        val answer: Array<Queue<Int>> = arrayOf(LinkedList(mutableListOf(0, 4, 6, infinity)), LinkedList(mutableListOf(1, 10, infinity)),
            LinkedList(mutableListOf(2, 7, 11, infinity)), LinkedList(mutableListOf(3, 8, infinity)), LinkedList(mutableListOf(5, infinity)),
            LinkedList(mutableListOf(9, infinity)), LinkedList(mutableListOf(12, infinity)))
        assertArrayEquals(answer, result)
    }

    @Test
    fun `optPreprocessing test with unique elements`() {
        val result = optPreprocessing(Process(7, 4, listOf(1, 2, 3, 4, 5, 6, 7)))
        val answer: Array<Queue<Int>> = arrayOf(LinkedList(mutableListOf(0, infinity)), LinkedList(mutableListOf(1, infinity)),
            LinkedList(mutableListOf(2, infinity)), LinkedList(mutableListOf(3, infinity)), LinkedList(mutableListOf(4, infinity)),
            LinkedList(mutableListOf(5, infinity)), LinkedList(mutableListOf(6, infinity)))
        assertArrayEquals(answer, result)
    }
}