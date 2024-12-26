import kotlin.math.pow

fun main() = Day25().run()

class Day25 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {

        val (keys, locks) = readInput(input)

        var matchCount = 0
        for (key in keys) {
            for (lock in locks) {
                var possible = true
                lock.zip(key) { a, b ->
                    if (a.digitToInt() + b.digitToInt() > 5) possible = false
                }
                if (possible) matchCount++
            }
        }

        return matchCount.toString()
    }

    override fun part2(input: List<String>): String {
        return "-"
    }

    private fun readInput(input: List<String>): Pair<List<String>, List<String>> {
        val keys = mutableListOf<String>()
        val locks = mutableListOf<String>()
        input.chunked(8) {
            var code = 0
            it.subList(1, 6).forEach {
                it.forEachIndexed { i, char ->
                    if (char == '#') code += 10.0.pow(4-i).toInt()
                }
            }
            val stringCode = code.toString().padStart(5, '0')
            if (it.first().contains("#")) {
                locks.add(stringCode)
            } else {
                keys.add(stringCode)
            }
        }
        return keys to locks
    }

}