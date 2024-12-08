fun main() = Day08().run()

class Day08 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val antennasByFrequency = readAntennaLocations(input).groupBy { it.frequency }
        val antinodes = antennasByFrequency.flatMap { (_, antennas) ->
            val antinodesForFrequency = mutableListOf<Pair<Int, Int>>()
            for (i in antennas.indices) {
                for (j in i + 1 until antennas.size) {
                    val distance = Pair(antennas[i].x - antennas[j].x, antennas[i].y - antennas[j].y)
                    antinodesForFrequency.add(Pair(antennas[i].x + distance.first, antennas[i].y + distance.second))
                    antinodesForFrequency.add(Pair(antennas[j].x - distance.first, antennas[j].y - distance.second))
                }
            }
            antinodesForFrequency
        }.filter {
            !(it.first < 0 || it.second < 0 || it.first >= input.first().length || it.second >= input.size)
        }.toSet().toList()
        return antinodes.count().toString()
    }

    override fun part2(input: List<String>): String {
        val antennasByFrequency = readAntennaLocations(input).groupBy { it.frequency }
        val antinodes = antennasByFrequency.flatMap { (_, antennas) ->
            val antinodesForFrequency = mutableListOf<Pair<Int, Int>>()
            for (i in antennas.indices) {
                antinodesForFrequency.add(Pair(antennas[i].x, antennas[i].y))
                for (j in i + 1 until antennas.size) {
                    val step = Pair(antennas[i].x - antennas[j].x, antennas[i].y - antennas[j].y)
                    var distance = step
                    while (true) {
                        val newAntinodes = listOf(
                            Pair(antennas[i].x + distance.first, antennas[i].y + distance.second),
                            Pair(antennas[j].x - distance.first, antennas[j].y - distance.second)
                        ).filter {
                            !(it.first < 0 || it.second < 0 || it.first >= input.first().length || it.second >= input.size)
                        }
                        if (newAntinodes.isEmpty()) break
                        antinodesForFrequency.addAll(newAntinodes)
                        distance = Pair(step.first + distance.first, step.second + distance.second)
                    }
                }
            }
            antinodesForFrequency
        }.toSet().toList()
        return antinodes.count().toString()
    }

    private fun readAntennaLocations(input: List<String>): List<Antenna> = input
        .mapIndexed { y, line ->
            line.toCharArray().mapIndexed { x, c ->
                if (c != '.') {
                    Antenna(x, y, c)
                } else null
            }.filterNotNull()
        }.flatten()

    data class Antenna(
        val x: Int,
        val y: Int,
        val frequency: Char
    )

}