import java.io.File

abstract class AbstractDailyPuzzle {

    fun run() {
        val inputDir = File("input")
        val outputDir = File("output")

        if (outputDir.exists()) outputDir.deleteRecursively()
        outputDir.mkdirs()

        val input1 = File("${inputDir.path}/input1.txt")
        val input2 = File("${inputDir.path}/input2.txt")
        val output1 = File("${outputDir.path}/output1.txt")
        val output2 = File("${outputDir.path}/output2.txt")

        output1.writeText(part1(input1.readLines()).joinToString("\n"))
        output2.writeText(part2(input2.readLines()).joinToString("\n"))
    }

    abstract fun part1(input: List<String>): List<String>

    abstract fun part2(input: List<String>): List<String>

}