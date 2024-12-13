fun main() = Day13().run()

class Day13 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val machines = readInput(input, 0L)
        return machines.sumOf {
            calculateSolution(it)
        }.toString()
    }

    override fun part2(input: List<String>): String {
        val machines = readInput(input, 10000000000000L)
        return machines.sumOf {
            val result = calculateSolution(it)
            result
        }.toString()
    }

    private fun calculateSolution(machine: Machine): Long {
        val a = (machine.buttons.second.x * machine.y - machine.buttons.second.y * machine.x).toDouble() /
                (machine.buttons.first.y * machine.buttons.second.x - machine.buttons.second.y * machine.buttons.first.x).toDouble()
        val b = (machine.x - a * machine.buttons.first.x) / machine.buttons.second.x
        if (a % 1 != 0.0 || b % 1 != 0.0) {
            return 0L
        }
        return a.toLong() * machine.buttons.first.cost + b.toLong() * machine.buttons.second.cost
    }

    private fun readInput(input: List<String>, extension: Long): List<Machine> {
        val buttons = mutableListOf<Button>()
        val machines = mutableListOf<Machine>()

        input
            .filterNot { it.isBlank() }
            .forEach {
                if (it.startsWith("Button")) {
                    val regexResult = ("Button ([AB]):\\s*X([+-]\\d*),\\s*Y([+-]\\d*)").toRegex().matchEntire(it)
                    val cost = if (regexResult!!.groups[1]!!.value == "A") 3 else 1
                    val x = regexResult.groups[2]!!.value.toInt()
                    val y = regexResult.groups[3]!!.value.toInt()
                    buttons.add(Button(x, y, cost))
                } else if (it.startsWith("Prize")) {
                    val regexResult = ("Prize: X=(\\d*), Y=(\\d*)").toRegex().matchEntire(it)
                    val x = regexResult!!.groups[1]!!.value.toLong()
                    val y = regexResult.groups[2]!!.value.toLong()
                    machines.add(Machine(Pair(buttons[0], buttons[1]), x + extension, y + extension))
                    buttons.clear()
                }
            }
        return machines
    }

    data class Machine(
        val buttons: Pair<Button, Button>,
        val x: Long,
        val y: Long,
    )

    data class Button(
        val x: Int,
        val y: Int,
        val cost: Int,
    )

}

