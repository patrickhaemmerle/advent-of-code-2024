fun main() = Day03().run()

class Day03 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val result = input.map { str ->
            "mul\\((\\d+),(\\d+)\\)".toRegex()
                .findAll(str)
                .map {
                    it.groups[1]!!.value.toLong() * it.groups[2]!!.value.toLong()
                }.toList().sum()
        }.sum()
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        var multiply = true
        val result = input.map { str ->
            "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)".toRegex()
                .findAll(str)
                .mapNotNull {
                    if (it.groups[0]!!.value == "do()") {
                        multiply = true
                        null
                    } else if (it.groups[0]!!.value == "don't()") {
                        multiply = false
                        null
                    } else if (multiply) {
                        it.groups[1]!!.value.toLong() * it.groups[2]!!.value.toLong()
                    } else {
                        null
                    }
                }.toList().sum()
        }.sum()
        return result.toString()
    }
}