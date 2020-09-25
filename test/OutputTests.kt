import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class OutputTests {

    @Test
    fun `output test with single string`() {
        val testFile = File("test/testsOutput/outputTest.txt").bufferedWriter()
        virtMem.output("Test", testFile)
        testFile.close()
        val outputResult = File("test/testsOutput/outputTest.txt").readLines()
        val answer = listOf("Test")
        assertEquals(answer, outputResult)
    }

    @Test
    fun `output test with several strings`() {
        val testFile = File("test/testsOutput/outputTest.txt").bufferedWriter()
        virtMem.output("Test1", testFile)
        virtMem.output("Test2", testFile)
        virtMem.output("Test3", testFile)
        testFile.close()
        val outputResult = File("test/testsOutput/outputTest.txt").readLines()
        val answer = listOf("Test1", "Test2", "Test3")
        assertEquals(answer, outputResult)
    }

    @Test
    fun `output test with empty string`() {
        val testFile = File("test/testsOutput/outputTest.txt").bufferedWriter()
        virtMem.output("Test1", testFile)
        virtMem.output("", testFile)
        virtMem.output("Test2", testFile)
        testFile.close()
        val outputResult = File("test/testsOutput/outputTest.txt").readLines()
        val answer = listOf("Test1", "", "Test2")
        assertEquals(answer, outputResult)
    }
}