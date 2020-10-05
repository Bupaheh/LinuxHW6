import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll

internal class InputProcessingTests {

    @Test
    fun `inputProcessing test`() {
        val result = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test1.txt"))
        val answerList: List <List<String>> = arrayListOf(arrayListOf("11 12", "1 2 3 10 12 14 15"))
        assertAll(
                { assertEquals(answerList, result.first) },
                { assertTrue(result.second) }
        )
    }

    @Test
    fun `inputProcessing test with batch processing`() {
        val result = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test2.txt"))
        val answerList: List <List<String>> = arrayListOf(arrayListOf("11 12", "5 12 1 3 2 14"),
                arrayListOf("5 4", "2 4 12 5 3"))
        assertAll(
            { assertEquals(answerList, result.first) },
            { assertTrue(result.second) }
        )
    }

    @Test
    fun `inputProcessing test with not existing file`() {
        val result = virtMem.inputProcessing(arrayOf("YEP.txt"))
        assertFalse(result.second)
    }
}