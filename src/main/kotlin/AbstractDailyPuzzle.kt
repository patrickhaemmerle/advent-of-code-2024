import java.io.File
import kotlin.time.measureTime

abstract class AbstractDailyPuzzle {

    fun run() {
        val inputDir = File("input")

        println("-".repeat(80))
        println("Running AoC with test input for ${this.javaClass.name}")
        println("-".repeat(80))

        val testInput1 = File("${inputDir.path}/testinput1.txt")
        val testInput2 = File("${inputDir.path}/testinput2.txt")
        val executionTimeTest = measureTime {
            println("Solution to part1 is: ${part1(testInput1.readLines())}")
            println("Solution to part2 is: ${part2(testInput2.readLines())}")
        }
        println("-".repeat(80))
        println(executionTimeTest)
        println("-".repeat(80))

        println("-".repeat(80))
        println("Running AoC for ${this.javaClass.name}")
        println("-".repeat(80))

        val input = File("${inputDir.path}/input.txt")
        val executionTime = measureTime {
            println("Solution to part1 is: ${part1(input.readLines())}")
            println("Solution to part2 is: ${part2(input.readLines())}")
        }
        println("-".repeat(80))
        println(executionTime)
    }

    abstract fun part1(input: List<String>): String

    abstract fun part2(input: List<String>): String

}