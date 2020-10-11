import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import virtMem.*
import java.io.File

internal class AlgoIterationTests {

    @Test
    fun `algoIteration test`() {
        val testData = Process(15, 4, listOf(15, 10, 6, 11, 10, 1, 6, 5, 6, 3, 10, 5, 13, 4, 5, 4, 3, 6, 5, 7))
        val outputFile = File("output.txt").bufferedWriter()
        algoIteration(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test4Ans.txt").readLines()
        val result = File("output.txt").readLines() + listOf("-")
        assertEquals(answer, result)
    }
}