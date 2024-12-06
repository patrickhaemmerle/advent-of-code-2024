import kotlin.math.absoluteValue

fun main() = Day01().run()

class Day01 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val lists = readInput(input)
        val first = lists.first.sorted()
        val second = lists.second.sorted()
        return first.zip(second) { a, b -> (a - b).absoluteValue }.sum().toString()
    }

    override fun part2(input: List<String>): String {
        val lists = readInput(input)
        val counts = Pair(
            lists.first.groupBy { it }.mapValues { it.value.size },
            lists.second.groupBy { it }.mapValues { it.value.size },
        )
        return counts.first.map {
            it.key * it.value * (counts.second[it.key] ?: 0)
        }.sum().toString()
    }

    private fun readInput(input: List<String>): Pair<List<Long>, List<Long>> {
        val lists = Pair(mutableListOf<Long>(), mutableListOf<Long>())
        input.forEach { line ->
            val items = line.split("\\s+".toRegex())
            lists.first.add(items[0].trim().toLong())
            lists.second.add(items[1].trim().toLong())
        }
        return lists
    }
}