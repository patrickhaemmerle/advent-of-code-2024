fun main() = Day5().run()

class Day5 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val rules = readRules(input)
        val updates = readUpdates(input)
        val result = updates.sumOf { update ->
            return@sumOf if (isValid(update, rules)) {
                update[update.size / 2].toInt()
            } else {
                0
            }
        }
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val rules = readRules(input)
        val updates = readUpdates(input)
        val result = updates.sumOf { update ->
            if (isValid(update, rules)) {
                0
            } else {
                val sortedUpdate = update.sortedWith { o1, o2 ->
                    val firstRule = rules[o1]
                    val secondRule = rules[o2]
                    // The case of contradicting rules is not handled (firstRule would take precedence)
                    if (firstRule != null && o2 in firstRule) {
                        -1
                    } else if (secondRule != null && o1 in secondRule) {
                        1
                    } else {
                        0
                    }
                }
                sortedUpdate[update.size / 2].toInt()
            }
        }
        return result.toString()
    }

    private fun isValid(
        update: List<String>,
        rules: Map<String, Set<String>>
    ): Boolean {
        val seenPages: MutableSet<String> = mutableSetOf()
        var valid = true
        for (i in update.indices) {
            val thisMustBeBefore = rules[update[i]] ?: emptySet()
            if (seenPages.any { it in thisMustBeBefore }) valid = false
            seenPages.add(update[i])
        }
        return valid
    }

    private fun readRules(input: List<String>): Map<String, Set<String>> {
        return input
            .filter { it.contains("|") }
            .map { it.split("|") }
            .groupBy { it.first() }
            .mapValues { (_, v) -> v.map { it[1] } }
            .mapValues { (_, v) -> v.toSet() }
    }

    private fun readUpdates(input: List<String>): List<List<String>> {
        return input
            .filter { it.contains(",") }
            .map { it.split(",") }
    }

}