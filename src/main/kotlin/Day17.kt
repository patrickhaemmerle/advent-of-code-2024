import kotlin.math.pow

fun main() = Day17().run()

class Day17 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val computer = readInput(input)
        return computer.execute()
    }

    override fun part2(input: List<String>): String {
        val computer = readInput(input)
        val expectedOutput = computer.program.joinToString(",")

        fun solve(position: Int = computer.program.size - 1): List<Long> {
            if (position < 0) return listOf(0L)

            val downStreamOutput = solve(position - 1)
            return (0..7).flatMap { threeBit ->
                downStreamOutput.mapNotNull { a ->
                    val candidate = a * 8 + threeBit
                    val output = Computer(computer.register.copy(A = candidate), computer.program).execute()
                    if (expectedOutput.substring(expectedOutput.length - position * 2 - 1) == output) candidate else null
                }
            }
        }

        val result = solve()
        return result.min().toString()
    }

    private fun readInput(input: List<String>): Computer {
        val registerA = input.single { it.startsWith("Register A:") }.split(":")[1].trim().toLong()
        val registerB = input.single { it.startsWith("Register B:") }.split(":")[1].trim().toLong()
        val registerC = input.single { it.startsWith("Register C:") }.split(":")[1].trim().toLong()
        val program = input.single { it.startsWith("Program:") }.split(":")[1].trim().split(",").map { it.toInt() }
        return Computer(Register(registerA, registerB, registerC), program)
    }
}

class Computer(
    val register: Register,
    val program: List<Int>,
) {

    private var pos = 0
    private val output = mutableListOf<Int>()

    fun execute(maxSteps: Int = -1): String {
        var executedSteps = 0
        while ((maxSteps < 0 || maxSteps > executedSteps) && pos < program.size - 1) {
            when (program[pos]) {
                0 -> adv(program[pos + 1])
                1 -> bxl(program[pos + 1])
                2 -> bst(program[pos + 1])
                3 -> jnz(program[pos + 1])
                4 -> bxc(program[pos + 1])
                5 -> out(program[pos + 1])
                6 -> bdv(program[pos + 1])
                7 -> cdv(program[pos + 1])
            }
            executedSteps++
        }
        return output.joinToString(",")
    }

    private fun resolveComboOperand(combo: Int): Long {
        return when {
            combo < 4 -> combo.toLong()
            combo == 4 -> register.A
            combo == 5 -> register.B
            combo == 6 -> register.C
            else -> error("Unknown Combo Operand: $combo")
        }

    }

    private fun adv(operand: Int) {
        register.A = div(operand)
        pos += 2
    }

    private fun div(operand: Int): Long {
        val comboOperand = resolveComboOperand(operand)
        val numerator = register.A
        val denominator = 2.toDouble().pow(comboOperand.toDouble()).toLong()
        return numerator / denominator
    }

    private fun bxl(operand: Int) {
        register.B = register.B xor operand.toLong()
        pos += 2
    }

    private fun bst(operand: Int) {
        val comboOperand = resolveComboOperand(operand)
        register.B = comboOperand % 8
        pos += 2
    }

    private fun jnz(operand: Int) {
        if (register.A == 0L) {
            pos += 2
            return
        }
        pos = operand
    }

    private fun bxc(operand: Int) {
        val result = register.B xor register.C
        register.B = result
        pos += 2
    }

    private fun out(operand: Int) {
        val result = resolveComboOperand(operand) % 8
        output.add(result.toInt())
        pos += 2
    }

    private fun bdv(operand: Int) {
        register.B = div(operand)
        pos += 2
    }

    private fun cdv(operand: Int) {
        register.C = div(operand)
        pos += 2
    }

}

data class Register(
    var A: Long,
    var B: Long,
    var C: Long,
)