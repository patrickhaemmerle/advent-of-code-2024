fun main() = Day14().run()

class Day14 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val dimensions = if (input.size < 15) {
            Pair(11, 7)
        } else {
            Pair(101, 103)
        }
        val robots = readInput(input).sortedBy { it.x }
        val newPos = robots.map {
            Robot(
                (((it.x + 100 * it.vX) % dimensions.first) + dimensions.first) % dimensions.first,
                (((it.y + 100 * it.vY) % dimensions.second) + dimensions.second) % dimensions.second,
                it.vX,
                it.vY
            )
        }.sortedBy { it.y }
        val quadrants = listOf(
            Quadrant(0, 0, dimensions.first / 2, dimensions.second / 2),
            Quadrant(dimensions.first / 2 + 1, 0, dimensions.first, dimensions.second / 2),
            Quadrant(0, dimensions.second / 2 + 1, dimensions.first / 2, dimensions.second),
            Quadrant(dimensions.first / 2 + 1, dimensions.second / 2 + 1, dimensions.first, dimensions.second),
        )
        val result = quadrants.map { quadrant ->
            newPos.count { robot ->
                robot.x >= quadrant.x0 && robot.x < quadrant.x1 && robot.y >= quadrant.y0 && robot.y < quadrant.y1
            }
        }.fold(1L) { acc: Long, i: Int -> acc * i }.toString()
        return result
    }

    override fun part2(input: List<String>): String {
        val dimensions = if (input.size < 15) {
            Pair(11L, 7L)
        } else {
            Pair(101L, 103L)
        }
        val robots = readInput(input).sortedBy { it.x }
        var newPos: List<Robot> = robots
        var steps = 0
        for (i in 0 until 10000) {
            val matrix = Array(dimensions.second.toInt()) { Array(dimensions.first.toInt()) { 0 } }
            newPos.forEach {
                matrix[it.y][it.x]++
            }
            if (matrix.flatMap { it.distinct() }.distinct().size < 3) {
                // This was just a bet :-) having max one robot per cell does not have to work, but it did with my
                // input. Alternatives include finding multiple neighbouring cells with min one robot. But there was
                // definitely some guesswork involved, because there's no description what a Christmas tree looks like.
                println("-------------------------------------------------------------------------------------------------")
                println(matrix.map { it.joinToString("") { if (it == 0) " " else it.toString() } }.joinToString("\n"))
                steps = i
                break
            }
            newPos = newPos.map {
                Robot(
                    ((((it.x + it.vX) % dimensions.first) + dimensions.first) % dimensions.first).toInt(),
                    ((((it.y + it.vY) % dimensions.second) + dimensions.second) % dimensions.second).toInt(),
                    it.vX,
                    it.vY
                )
            }
        }
        return steps.toString()
    }

    private fun readInput(input: List<String>): List<Robot> =
        input.map { line ->
            val match = "p=(\\d+),(\\d+)\\s+v=(-?\\d+),(-?\\d+)".toRegex().matchEntire(line)
            Robot(
                match!!.groups[1]!!.value.toInt(),
                match.groups[2]!!.value.toInt(),
                match.groups[3]!!.value.toInt(),
                match.groups[4]!!.value.toInt(),
            )
        }

    data class Robot(
        val x: Int,
        val y: Int,
        val vX: Int,
        val vY: Int,
    )

    data class Quadrant(
        val x0: Int,
        val y0: Int,
        val x1: Int,
        val y1: Int
    )

}

