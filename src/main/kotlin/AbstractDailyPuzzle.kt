import java.io.File

abstract class AbstractDailyPuzzle {

    fun run() {
        println("Runnin AoC for ${this.javaClass.name}")
        println("-".repeat(80))

        val inputDir = File("input")

        val input1 = File("${inputDir.path}/input1.txt")
        val input2 = File("${inputDir.path}/input2.txt")

        println("Solution to part1 is: ${part1(input1.readLines())}")
        println("Solution to part2 is: ${part2(input2.readLines())}")
        println("-".repeat(80))
    }

    abstract fun part1(input: List<String>): String

    abstract fun part2(input: List<String>): String

}