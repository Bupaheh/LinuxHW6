import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import virtMem.generateTest
import virtMem.main
import java.io.File

internal class GenerateTestTests {

    @Test
    fun `generateTest test`() {
        generateTest(arrayOf("gen", "10", "5", "12"), File("outputTest.txt").bufferedWriter())
        val resultLines = File("outputTest.txt").readLines()
        val firstLine = resultLines[0].split(" ")
        val secondLine = resultLines[1].split(" ")
        assertTrue(firstLine.size == 2 && firstLine.all { element ->
            element.toIntOrNull() != null} && firstLine.first() == "10" && firstLine.last() == "5"
                && secondLine.all { element -> element.toIntOrNull() != null && element.toInt() > 0 })
    }

    @Test
    fun `generateTest test with incorrect arguments`() {
        generateTest(arrayOf("gen", "10", "0", "12"), File("outputTest.txt").bufferedWriter())
        val resultLines = File("outputTest.txt").readLines()
        assertEquals(listOf("Error: Incorrect generation arguments"), resultLines)
    }
}