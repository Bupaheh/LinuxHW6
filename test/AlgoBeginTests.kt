import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import virtMem.inputProcessing
import virtMem.*
import java.io.File

internal class AlgoBeginTests {

    @Test
    fun `main test with single request`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test1.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test1Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with batch processing`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test2.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test2Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with random generated data`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test3.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test3Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with random generated data 2`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test4.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test4Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with not correct data`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test5.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = listOf("Error: Incorrect number of variables on the first line", "-")
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }

    @Test
    fun `main test with a lot of errors`() {
        val outputFile = File("output.txt").bufferedWriter()
        val (testData, _) = inputProcessing(arrayOf("data/test6.txt"))
        algoBegin(testData, outputFile)
        outputFile.close()
        val answer = File("data/Results of examples/test6Ans.txt").readLines()
        val result = File("output.txt").readLines()
        assertEquals(answer, result)
    }
}