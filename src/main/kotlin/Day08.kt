fun main() = Day08().run()

class Day08 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val antennasByFrequency = readAntennaLocations(input).groupBy { it.frequency }
        val mapDimensionX = input.first().length
        val mapDimensionY = input.size

        val antiNodes = antennasByFrequency.flatMap { (_, antennas) ->
            antennas.mapIndexed { i, firstAntenna ->
                antennas.subList(i + 1, antennas.size).map { secondAntenna ->
                    val xDistance = firstAntenna.x - secondAntenna.x
                    val yDistance = firstAntenna.y - secondAntenna.y
                    listOf(
                        AntiNode(firstAntenna.x + xDistance, firstAntenna.y + yDistance),
                        AntiNode(secondAntenna.x - xDistance, secondAntenna.y - yDistance),
                    ).filter { coordinateIsWithinMap(it.x, it.y, mapDimensionX, mapDimensionY) }
                }.flatten()
            }.flatten()
        }.toSet().toList()
        return antiNodes.count().toString()
    }

    override fun part2(input: List<String>): String {
        val antennasByFrequency = readAntennaLocations(input).groupBy { it.frequency }
        val mapDimensionX = input.first().length
        val mapDimensionY = input.size

        val antiNodes = antennasByFrequency.flatMap { (_, antennas) ->
            antennas.mapIndexed { i, firstAntenna ->
                listOf(AntiNode(firstAntenna.x, firstAntenna.y)) +
                        antennas.subList(i + 1, antennas.size)
                            .map { secondAntenna ->
                                val xStep = firstAntenna.x - secondAntenna.x
                                val yStep = firstAntenna.y - secondAntenna.y
                                var xDistance = xStep
                                var yDistance = yStep
                                val antiNodesForThisPairOfAntennas = mutableListOf<AntiNode>()
                                while (true) {
                                    val newAntinodes = listOf(
                                        AntiNode(firstAntenna.x + xDistance, firstAntenna.y + yDistance),
                                        AntiNode(secondAntenna.x - xDistance, secondAntenna.y - yDistance),
                                    ).filter { coordinateIsWithinMap(it.x, it.y, mapDimensionX, mapDimensionY) }

                                    if (newAntinodes.isEmpty()) break

                                    antiNodesForThisPairOfAntennas.addAll(newAntinodes)
                                    xDistance += xStep
                                    yDistance += yStep
                                }
                                antiNodesForThisPairOfAntennas

                            }.flatten()
            }.flatten()
        }.toSet().toList()
        return antiNodes.count().toString()
    }

    private fun coordinateIsWithinMap(x: Int, y: Int, xDimension: Int, yDimension: Int) =
        x >= 0 && y >= 0 && x < xDimension && y < yDimension

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
        val frequency: Char,
    )

    data class AntiNode(
        val x: Int,
        val y: Int,
    )

}