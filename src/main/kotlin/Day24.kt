fun main() = Day24().run()

class Day24 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val device = readInput(input)
        val result = device.solve().toLong(2)
        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val device = readInput(input)
        val actualResult = device.solve()
        val expectedResult = evaluateExpectedResult(device).padStart(actualResult.length, '0')

//        for (i in 0 until expectedResult)
//        val candidates = device.gates
//            .filter { it.key.startsWith("z") }
//            .map { findCandidatePairsToSwap(it.key, device) }
//            .toList().reversed()
//            .mapIndexed { i, it ->
//                if (expectedResult[i] == actualResult[i]) null else it
//            }.filterNotNull()

        return "-"
    }

    private fun findCandidatePairsToSwap(endPoint: String, device: Device): Set<Pair<String, String>> {
        val candidates = findUpstreamCandidatesToSwap(endPoint, device).toList()
        val candidatePairs = mutableSetOf<Pair<String, String>>()
        for (i in candidates.indices) {
            for (j in i + 1 until candidates.size) {
                if (device.solveForValueOnWire(candidates[i]) != device.solveForValueOnWire(candidates[j])) {
                    if (candidates[i] < candidates[j]) candidatePairs.add(candidates[i] to candidates[j])
                    else candidatePairs.add(candidates[j] to candidates[i])
                }
            }
        }
        val otherEndpoints = device.gates.keys
            .filter { it.startsWith("z") }
            .filter { device.solveForValueOnWire(it) != device.solveForValueOnWire(endPoint) }
            .map { endPoint to it }
        return candidatePairs + otherEndpoints
    }

    private fun findUpstreamCandidatesToSwap(start: String, device: Device): Set<String> {
        if (!device.gates.contains(start)) return emptySet()
        return findUpstreamCandidatesToSwap(
            device.gates[start]!!.first,
            device
        ) + findUpstreamCandidatesToSwap(
            device.gates[start]!!.second,
            device
        ) + setOf(start)
    }

    private fun performHealthCheck(device: Device): Boolean {
        val expectedResult = evaluateExpectedResult(device)
        val actualResult = device.solve()
        return actualResult == expectedResult
    }

    private fun evaluateExpectedResult(device: Device): String {
        val summand1 = device.input
            .filter { it.key.startsWith("x") }
            .map { if (it.value) "1" else "0" }
            .reversed()
            .joinToString("")
        val summand2 = device.input
            .filter { it.key.startsWith("y") }
            .map { if (it.value) "1" else "0" }
            .reversed()
            .joinToString("")

        check(summand1.toLong(2) < Long.MAX_VALUE / 2)
        check(summand2.toLong(2) < Long.MAX_VALUE / 2)

        val expectedResult = (summand1.toLong(2) + summand2.toLong(2)).toString(2)
        return expectedResult
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
                Operator.OR -> operand1 || operand2
                Operator.AND -> operand1 && operand2
                Operator.XOR -> operand1 != operand2
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

