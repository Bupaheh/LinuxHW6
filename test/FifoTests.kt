import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import virtMem.*

internal class FifoTests {

    @Test
    fun `fifo test`() {
        val result = fifo(Process(7, 4, listOf(1, 2, 3, 4, 3, 4, 4, 7, 5, 4 ,6, 1, 2)))
        val answer = Pair("1 2 3 4 0 0 0 1 2 0 3 4 1", 9)
        assertEquals(answer, result)
    }

    @Test
    fun `fifo random generated data test`() {
        val result = fifo(Process(15, 4, listOf(15, 10, 6, 11, 10, 1, 6, 5, 6, 3, 10, 5, 13, 4, 5, 4, 3, 6, 5, 7)))
        val answer = Pair("1 2 3 4 0 1 0 2 0 3 4 0 1 2 3 0 4 1 0 2", 14)
        assertEquals(answer, result)
    }
}