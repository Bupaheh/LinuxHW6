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
    fun `lru mini test`() {
        val result = lru(Process(5, 3, listOf(1, 2, 2, 3, 1, 4, 1, 3, 5)))
        val answer = Pair("1 2 0 3 0 2 0 0 2", 5)
        assertEquals(answer, result)
    }
}