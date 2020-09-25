package virtMem
import java.io.BufferedWriter
import java.io.File

fun output(outputString: String, outputFile: BufferedWriter) {
    outputFile.write(outputString)
    outputFile.newLine()
}

fun main(args: Array <String>) {
    val outputFile = File("output.txt").bufferedWriter()
}