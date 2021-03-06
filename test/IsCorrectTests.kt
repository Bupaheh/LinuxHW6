import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import virtMem.*

internal class IsCorrectTests {

    @Test
    fun `isCorrect test with correct input`() {
        assertEquals(ToProcessErrors.Correct, isCorrect(listOf("5", "7"), listOf("54", "345", "5435", "234")))
    }

    @Test
    fun `isCorrect test with wrong first argument size`() {
        assertEquals(ToProcessErrors.IncorrectNumberOfVariablesOnTheFirstLine, isCorrect(listOf("5", "7", "3"), listOf("54", "345", "5435", "234")))
    }

    @Test
    fun `isCorrect test with incorrect first argument list's elements`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheFirstLine, isCorrect(listOf("5", "0"), listOf("54", "345", "5435", "234")))
    }

    @Test
    fun `isCorrect test with incorrect second argument list's elements`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheSecondLine, isCorrect(listOf("5", "7"), listOf("54", "-2", "5435", "234")))
    }

    @Test
    fun `isCorrect test with not integers in lists`() {
        assertEquals(ToProcessErrors.NotPositiveNumberOnTheFirstLine, isCorrect(listOf("5", "323k1"), listOf("54", "345", "5435", "234")))
    }
}