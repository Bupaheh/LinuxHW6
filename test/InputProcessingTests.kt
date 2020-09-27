import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertAll

internal class InputProcessingTests {

    @Test
    fun `inputProcessing test`() {
        val resultList: ArrayList <ArrayList<String>> = arrayListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test1.txt"), resultList)
        val answerList: ArrayList <ArrayList<String>> = arrayListOf(arrayListOf("11 12", "1 2 3 10 12 14 15"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with batch processing`() {
        val resultList: ArrayList <ArrayList<String>> = arrayListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("test/inputProcessingTestsInput/test2.txt"), resultList)
        val answerList: ArrayList <ArrayList<String>> = arrayListOf(arrayListOf("11 12", "5 12 1 3 2 14"),
                arrayListOf("5 4", "2 4 12 5 3"))
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertTrue(resultBool) }
        )
    }

    @Test
    fun `inputProcessing test with not existing file`() {
        val resultList: ArrayList <ArrayList<String>> = arrayListOf()
        val resultBool = virtMem.inputProcessing(arrayOf("YEP.txt"), resultList)
        val answerList: ArrayList <ArrayList<String>> = arrayListOf()
        assertAll(
                { assertEquals(answerList, resultList) },
                { assertFalse(resultBool) }
        )
    }
}