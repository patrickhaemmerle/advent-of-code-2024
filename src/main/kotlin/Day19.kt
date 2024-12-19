fun main() = Day19().run()

class Day19 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val towels = towelList(input)
        val patterns = patterns(input)
        val cache = mutableMapOf<String, Boolean>()
        val hasSolution = patterns.map {
            hasSolution(it, towels, cache)
        }
        return hasSolution.filter { it }.count().toString()
    }

    override fun part2(input: List<String>): String {
        val towels = towelList(input)
        val patterns = patterns(input)
        val cache = mutableMapOf<String, Long>()
        val result = patterns.sumOf {
            numberOfSolutions(it, towels, cache)
        }
        return result.toString()
    }

    private fun hasSolution(
        targetPattern: String,
        towels: List<String>,
        cache: MutableMap<String, Boolean>
    ): Boolean {
        if (targetPattern.isBlank()) return true
        if (targetPattern in cache) return cache[targetPattern]!!

        val hasSolution = towels
            .filter {
                targetPattern.startsWith(it)
            }.firstOrNull {
                hasSolution(targetPattern.substring(it.length), towels, cache)
            } != null
        cache[targetPattern] = hasSolution
        return hasSolution
    }

    private fun numberOfSolutions(
        targetPattern: String,
        towels: List<String>,
        cache: MutableMap<String, Long>
    ): Long {
        if (targetPattern.isBlank()) return 1
        if (targetPattern in cache) return cache[targetPattern]!!

        val numberOfSolutions = towels.filter { targetPattern.startsWith(it) }
            .sumOf {
                numberOfSolutions(targetPattern.substring(it.length), towels, cache)
            }
        cache[targetPattern] = numberOfSolutions
        return numberOfSolutions
    }

    private fun towelList(input: List<String>) = input.first().split(",").map { it.trim() }

    private fun patterns(input: List<String>) = input.subList(2, input.size)
}