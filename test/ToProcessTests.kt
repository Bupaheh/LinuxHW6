import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import virtMem.*

internal class ToProcessTests {

    @Test
    fun `toProcess test with correct data`() {
        val result = toProcess(arrayListOf("6 1", "43 12 65 3")).first
        val answer = Process(6, 1, listOf(43, 12, 65, 3))
        assertEquals(answer, result)
    }

    @Test
    fun `toProcess test with wrong list size`() {
        assertEquals(ToProcessErrors.IncorrectNumberOfRows, toProcess(arrayListOf("6 1", "43 12 65 3", "1")).second)
    }

    @Test
    fun `toProcess test with incorrect first element`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheFirstLine, toProcess(arrayListOf("6 f", "43 12 65 3")).second)
    }

    @Test
    fun `toProcess test with not integers in list`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheSecondLine, toProcess(arrayListOf("6 1", "43 1f2 65 3")).second)
    }

    @Test
    fun `toProcess test with zero`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheSecondLine, toProcess(arrayListOf("6 1", "43 12 0 3")).second)
    }
}