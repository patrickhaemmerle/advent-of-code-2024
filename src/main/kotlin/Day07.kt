fun main() = Day07().run()

class Day07 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val equations = readInput(input)
        val possibleOperators = listOf("*", "+")
        val result = equations.sumOf { equation ->
            if (isEquationSolvable(equation, possibleOperators)) {
                equation.result
            } else {
                0L
            }
        }
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val equations = readInput(input)
        val possibleOperators = listOf("*", "+", "||")
        val result = equations.sumOf { equation ->
            if (isEquationSolvable(equation, possibleOperators)) {
                equation.result
            } else {
                0L
            }
        }
        return result.toString()
    }

    private fun readInput(input: List<String>) = input.map { line ->
        Equation(
            line.split(":").first().trim().toLong(),
            line.split(":")[1].trim().split("\\s+".toRegex()).map { it.trim().toLong() }
        )
    }

    private fun isEquationSolvable(equation: Equation, possibleOperators: List<String>): Boolean {
        val numberOfOperators = equation.operands.size - 1
        val possibleOperatorCombinations = findPossibleCombinations(possibleOperators, numberOfOperators)
        for (i in possibleOperatorCombinations.indices) {
            var result = equation.operands.first()
            for (j in possibleOperatorCombinations[i].indices) {
                when (possibleOperatorCombinations[i][j]) {
                    "*" -> result *= equation.operands[j + 1]
                    "+" -> result += equation.operands[j + 1]
                    "||" -> result = ("${result}${equation.operands[j + 1]}").toLong()
                }
                if (result > equation.result) break
            }
            if (result == equation.result) return true
        }
        return false
    }

    private fun findPossibleCombinations(possibleOperators: List<String>, remainingSlots: Int): List<List<String>> {
        if (remainingSlots == 1) return possibleOperators.map { listOf(it) }
        val possibleRemainingCombinations = findPossibleCombinations(possibleOperators, remainingSlots - 1)
        val res = possibleOperators.flatMap { operator -> possibleRemainingCombinations.map { listOf(operator) + it } }
        return res
    }

    private data class Equation(
        val result: Long,
        val operands: List<Long>
    )

}