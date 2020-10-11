import org.junit.jupiter.api.Test
import virtMem.*
import org.junit.jupiter.api.Assertions.*

internal class OptTests {

    @Test
    fun `opt test`() {
        val result = opt(Process(7, 4, listOf(1, 2, 3, 4, 1, 5, 1, 3, 4, 6, 2, 3, 7))).second
        assertEquals(8, result)
    }

    @Test
    fun `opt random generated data test`() {
        val result = opt(Process(15, 4, listOf(15, 10, 6, 11, 10, 1, 6, 5, 6, 3, 10, 5, 13, 4, 5, 4, 3, 6, 5, 7)))
        val answer = Pair("1 2 3 4 0 1 0 4 0 1 0 0 2 2 0 0 0 0 0 2", 10)
        assertEquals(answer, result)
    }
}