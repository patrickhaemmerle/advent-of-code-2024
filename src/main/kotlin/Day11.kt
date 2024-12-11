fun main() = Day11().run()

class Day11 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        return readInput(input).sumOf { calculateNumberOfStones(it, 25, mutableMapOf()) }.toString()
    }

    override fun part2(input: List<String>): String {
        return readInput(input).sumOf { calculateNumberOfStones(it, 75, mutableMapOf()) }.toString()
    }

    private fun calculateNumberOfStones(number: Long, level: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long {
        if (level == 0) {
            return 1L
        }
        val cacheKey = Pair(number, level)
        if (cacheKey in cache) return cache[cacheKey]!!

        val numberAsString = number.toString()
        val nextNumbers = when {
            number == 0L -> listOf(1L)
            numberAsString.length % 2 == 0 -> {
                listOf(
                    numberAsString.substring(0, numberAsString.length / 2).toLong(),
                    numberAsString.substring(numberAsString.length / 2).toLong(),
                )
            }

            else -> listOf(number * 2024L)
        }
        return nextNumbers
            .sumOf { calculateNumberOfStones(it, level - 1, cache) }
            .also { cache[cacheKey] = it }
    }

    private fun readInput(input: List<String>) = input.first().split("\\s+".toRegex()).map { it.toLong() }
}