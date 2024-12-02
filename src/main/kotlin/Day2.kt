import kotlin.math.absoluteValue

fun main() = Day2().run()

class Day2 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val result = input.map { readInput(it) }
            .map { isReportSafe(it) }.filter { it }.size
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val result = input.map { readInput(it) }
            .map { report ->
                if (!isReportSafe(report)) {
                    // TODO This is very brute force, needs to be fixed :-/
                    for (i: Int in report.indices) {
                        if (isReportSafe(report.subList(0, i) + report.subList(i + 1, report.size))) {
                            return@map true
                        }
                    }
                    return@map false
                } else {
                    true
                }
            }.filter { it }.size
        return result.toString()
    }

    private fun isReportSafe(report: List<Long>): Boolean {
        val reportIsIncreasing = report[1] > report[0]
        for (i: Int in 1..<report.size) {
            val diff = (report[i] - report[i - 1]).absoluteValue
            val thisLevelIsIncreasing = report[i] > report[i - 1]
            if (diff < 1 || diff > 3 || thisLevelIsIncreasing != reportIsIncreasing) {
                return false
            }
        }
        return true
    }

    private fun readInput(input: String): List<Long> = input.split("\\s+".toRegex()).map { it.toLong() }
}