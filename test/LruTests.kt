import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import virtMem.Process
import virtMem.*

internal class LruTests {

    @Test
    fun `lru test`() {
        val result = lru(Process(7, 4, listOf(1, 2, 3, 4, 1, 5, 1, 3, 4, 6, 2, 3, 7)))
        val answer = Pair("1 2 3 4 0 2 0 0 0 2 1 0 4", 8)
        assertEquals(answer, result)
    }

    @Test
    fun `lru random generated data test`() {
        val result = lru(Process(15, 4, listOf(15, 10, 6, 11, 10, 1, 6, 5, 6, 3, 10, 5, 13, 4, 5, 4, 3, 6, 5, 7)))
        val answer = Pair("1 2 3 4 0 1 0 4 0 2 1 0 3 2 0 0 1 3 0 2", 13)
        assertEquals(answer, result)
    }
}