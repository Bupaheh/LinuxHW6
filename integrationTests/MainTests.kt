import org.junit.jupiter.api.Test
import virtMem.*

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class MainTests {

    @Test
    fun `main test with single request`() {
        main(arrayOf("data/test1.txt"))
        val answer = File("data/Results of examples/test1Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with batch processing`() {
        main(arrayOf("data/test2.txt"))
        val answer = File("data/Results of examples/test2Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with random generated data`() {
        main(arrayOf("data/test3.txt"))
        val answer = File("data/Results of examples/test3Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with random generated data 2`() {
        main(arrayOf("data/test4.txt"))
        val answer = File("data/Results of examples/test4Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with not existing file`() {
        main(arrayOf("something"))
        val answer = listOf("Error: File doesn't exist")
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with not correct data`() {
        main(arrayOf("data/test5.txt"))
        val answer = listOf("Error: Incorrect number of variables on the first line", "-")
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with test generation`() {
        main(arrayOf("gen", "10", "5", "12"))
        val resultLines = File("outputTest.txt").readLines()
        val firstLine = resultLines[0].split(" ")
        val secondLine = resultLines[1].split(" ")
        assertTrue(firstLine.size == 2 && firstLine.all { element ->
            element.toIntOrNull() != null} && firstLine.first() == "10" && firstLine.last() == "5"
            && secondLine.all { element -> element.toIntOrNull() != null && element.toInt() > 0 })
    }

    @Test
    fun `main test with incorrect generation data`() {
        main(arrayOf("gen", "-2", "5", "12"))
        val resultLines = File("outputTest.txt").readLines()
        val answer = listOf("Error: Incorrect generation arguments")
        assertEquals(answer, resultLines)
    }

    @Test
    fun `main test with a lot of errors`() {
        main(arrayOf("data/test6.txt"))
        val answer = File("data/Results of examples/test6Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with a lot of data`() {
        main(arrayOf("data/test7.txt"))
        val answer = File("data/Results of examples/test7Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }
}