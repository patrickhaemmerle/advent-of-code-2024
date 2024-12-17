import kotlin.math.pow

fun main() = Day17().run()

class Day17 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val computer = readInput(input)
        return computer.execute()
    }

    override fun part2(input: List<String>): String {
        // For some not yet known reasons... two implementations of Computer do not yield the desired result
        // With both implementations, there is no way to make it spit out a 5, but there are two ways to get a 4

        // The first who finds the issue with this code is eligible to get a free beer from me :-D

        println("-".repeat(80))
        (0L .. 7).forEach {
            println("output ${execute(input, it)} -> input $it")
        }
        println("-".repeat(80))
        val computer = readInput(input)
        (0L .. 7).forEach {
            val result = Computer(computer.register.copy(A=it), computer.program).execute()
            println("output $result -> input $it")
        }
        println("-".repeat(80))
        return execute(input)
    }

    private fun execute(input: List<String>, aOverride: Long? = null): String {
        var a = aOverride ?: input.single { it.startsWith("Register A:") }.split(":")[1].trim().toLong()
        var b = input.single { it.startsWith("Register B:") }.split(":")[1].trim().toLong()
        var c = input.single { it.startsWith("Register C:") }.split(":")[1].trim().toLong()
        val program = input.single { it.startsWith("Program:") }.split(":")[1].trim().split(",").map { it.toInt() }
        val output = mutableListOf<Int>()

        fun resolveCombo(operand: Int): Long {
            return when (operand) {
                4 -> a
                5 -> b
                6 -> c
                else -> operand.toLong()
            }
        }

        var pos = 0
        while (pos < program.size) {
            when (program[pos]) {
                0 -> a = a / 2.0.pow(resolveCombo(program[pos + 1]).toDouble()).toLong()
                1 -> b = b xor program[pos + 1].toLong()
                2 -> b = resolveCombo(program[pos + 1]) % 8
                3 -> if (a != 0L) pos = program[pos + 1] - 2
                4 -> b = b xor c
                5 -> output.add((resolveCombo(program[pos+1]) % 8).toInt())
                6 -> b = a / 2.0.pow(resolveCombo(program[pos + 1]).toDouble()).toLong()
                7 -> c = a / 2.0.pow(resolveCombo(program[pos + 1]).toDouble()).toLong()
            }
            pos += 2
        }
        return output.joinToString(",")
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