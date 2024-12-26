import Day24.Operator.*

fun main() = Day24().run()

class Day24 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val device = readInput(input)
        val result = device.solve().toLong(2)
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        // This code validates the addition machine, errors are reported and fixes need to be made manually
        if (input.size == 0) return "-"
        val device = readInput(input)
        var remainder = "hfm"
        for (i in 1 until 45) {
            println("testing $i")

            // AND and XOR
            val level1 = device.gates
                .filter {
                    (
                            it.value.first == "x" + (i.toString().padStart(2, '0'))
                                    || it.value.first == "y" + (i.toString().padStart(2, '0'))
                            ) && (
                            it.value.second.endsWith(i.toString().padStart(2, '0'))
                                    || it.value.second.endsWith(i.toString().padStart(2, '0'))
                            ) && it.value.first != it.value.second
                }
            check(level1.size == 2)
            check(level1.filter { it.value.third == XOR }.size == 1)
            check(level1.filter { it.value.third == AND }.size == 1)
            val level1Xor = level1.filter { it.value.third == XOR }.map { it.key }.single()
            val level1And = level1.filter { it.value.third == AND }.map { it.key }.single()

            // Remainder level 1
            val remainderLevel1Xor =
                device.gates.filter { (it.value.first == level1Xor && it.value.second == remainder) || (it.value.first == remainder && it.value.second == level1Xor) }
                    .filter { it.value.third == XOR }
                    .toList().single()
            check (remainderLevel1Xor.first == "z${i.toString().padStart(2, '0')}")

            val remainderLevel1And =
                device.gates.filter { (it.value.first == level1Xor && it.value.second == remainder) || (it.value.first == remainder && it.value.second == level1Xor) }
                    .filter { it.value.third == AND }
                    .toList().single()
            val remainderLevel2Or =
                device.gates.filter { (it.value.first == level1And && it.value.second == remainderLevel1And.first) || (it.value.first == remainderLevel1And.first && it.value.second == level1And) }
                    .filter { it.value.third == OR }
                    .toList().single()
            remainder = remainderLevel2Or.first
        }
        return "-"
    }

    private fun readInput(input: List<String>): Device {
        val knownValues = input
            .filter { it.contains(":") }
            .map { it.split("\\s*:\\s*".toRegex()) }
            .associateBy { it[0] }
            .mapValues {
                it.value[1] == "1"
            }
        val gates = input
            .filter { it.contains("->") }
            .map { it.split("\\s*->\\s*".toRegex()) }
            .associateBy { it[1] }
            .mapValues {
                val parts = it.value[0].split("\\s+".toRegex())
                Triple(parts[0], parts[2], Operator.valueOf(parts[1]))
            }
        return Device(knownValues, gates)
    }

    private class Device(
        val input: Map<String, Boolean>,
        val gates: Map<String, Triple<String, String, Operator>>
    ) {
        private val knownValues = input.toMutableMap()

        fun solveForValueOnWire(wire: String): Boolean {
            if (wire in knownValues) return knownValues[wire]!!

            val target = gates[wire] ?: error("Wire does not exist")
            val operand1 = solveForValueOnWire(target.first)
            val operand2 = solveForValueOnWire(target.second)
            val result = when (target.third) {
                OR -> operand1 || operand2
                AND -> operand1 && operand2
                XOR -> operand1 != operand2
            }
            knownValues[wire] = result
            return result
        }

        fun solve(): String = gates
            .asSequence()
            .filter { it.key.startsWith("z") }
            .map { it.key }
            .sortedDescending()
            .map { solveForValueOnWire(it) }
            .joinToString("") { if (it) "1" else "0" }

    }

    private enum class Operator {
        OR, AND, XOR;
    }

}

