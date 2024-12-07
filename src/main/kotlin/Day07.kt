fun main() = Day07().run()

class Day07 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val equations = readInput(input)
        val possibleOperators = listOf("*", "+")
        val result = equations.sumOf { equation ->
            solveEquationIterative(equation, possibleOperators)
        }
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val equations = readInput(input)
        val possibleOperators = listOf("*", "+", "||")
        val result = equations.sumOf { equation ->
            solveEquationIterative(equation, possibleOperators)
        }
        return result.toString()
    }

    private fun readInput(input: List<String>) = input.map { line ->
        Equation(
            line.split(":").first().trim().toLong(),
            line.split(":")[1].trim().split("\\s+".toRegex()).map { it.trim().toLong() }
        )
    }

    private fun solveEquationIterative(equation: Equation, possibleOperators: List<String>): Long {
        val processList = ArrayDeque<IntermediateResult>()
        processList.addLast(
            IntermediateResult(
                equation.operands.first(),
                equation.operands.subList(1, equation.operands.size),
            )
        )

        while (processList.isNotEmpty()) {
            val current = processList.removeFirst()
            possibleOperators.forEach { operator ->
                val nextResult = when (operator) {
                    "*" -> current.result * current.remainingOperands.first()
                    "||" -> ("${current.result}${current.remainingOperands.first()}").toLong()
                    "+" -> current.result + current.remainingOperands.first()
                    else -> error("Unknown  Operator")
                }

                val remainingOperands = current.remainingOperands.subList(1, current.remainingOperands.size)

                if (nextResult == equation.targetResult && remainingOperands.isEmpty()) return nextResult

                if (nextResult <= equation.targetResult && remainingOperands.isNotEmpty()) {
                    processList.addLast(IntermediateResult(nextResult, remainingOperands))
                }
            }
        }
        return 0L
    }

    private data class Equation(
        val targetResult: Long,
        val operands: List<Long>
    )

    private data class IntermediateResult(
        val result: Long,
        val remainingOperands: List<Long>,
    )

}