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
    fun `opt mini test`() {
        val result = opt(Process(5, 3, listOf(1, 2, 3, 3, 1, 4, 5, 3, 1, 2, 4))).second
        assertEquals(7, result)
    }
}