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
}