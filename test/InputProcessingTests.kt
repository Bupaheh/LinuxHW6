import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll

internal class InputProcessingTests {

    @Test
    fun `inputProcessing test with all requests in one string`() {
        val resultList: MutableList <MutableList<String>> = mutableListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test1.txt"), resultList)
        val answerList: List<List <String>> = listOf(listOf("11 12", "1 2 3 10 12 14 15"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with all requests in different rows`() {
        val resultList: MutableList <MutableList<String>> = mutableListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test2.txt"), resultList)
        val answerList: List<List <String>> = listOf(listOf("12 13", "1", "2", "4", "5", "12"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with mixed input format`() {
        val resultList: MutableList <MutableList<String>> = mutableListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test3.txt"), resultList)
        val answerList: List<List <String>> = listOf(listOf("11 12", "5", "12 1", "3 2", "14"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with batch processing`() {
        val resultList: MutableList <MutableList<String>> = mutableListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test4.txt"), resultList)
        val answerList: List<List <String>> = listOf(listOf("11 12", "5", "12 1", "3 2", "14"),
                listOf("5 4", "2 4", "12", "5 3"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with not existing file`() {
        val resultList: MutableList <MutableList<String>> = mutableListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("YEP.txt"), resultList)
        val answerList: List<List <String>> = listOf()
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertFalse(resultBool) }
        )
    }
}