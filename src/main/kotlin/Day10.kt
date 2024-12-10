fun main() = Day10().run()

class Day10 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val map = readInput(input)

        var score = 0
        for (i in map.indices) {
            for (j in map.first().indices) {
                if (map[i][j] == '0') {
                    score += discoverReachablePeaks(Position(i, j), map).toSet().count()
                }
            }
        }
        return score.toString()
    }

    override fun part2(input: List<String>): String {
        val map = readInput(input)

        var score = 0
        for (i in map.indices) {
            for (j in map.first().indices) {
                if (map[i][j] == '0') {
                    score += discoverReachablePeaks(Position(i, j), map).count()
                }
            }
        }
        return score.toString()
    }

    private fun discoverReachablePeaks(startingPoint: Position, map: List<CharArray>): List<Position> {
        val toDiscover = ArrayDeque<Position>()
        val discovered = mutableListOf<Position>()

        toDiscover.addLast(startingPoint)

        while (toDiscover.isNotEmpty()) {
            val current = toDiscover.removeFirst()
            discovered.add(current)
            listOf(
                Position(current.i + 1, current.j),
                Position(current.i - 1, current.j),
                Position(current.i, current.j + 1),
                Position(current.i, current.j - 1),
            ).mapNotNull { if (isAdjacent(current, it, map)) it else null }
                .filterNot { it in discovered }
                .forEach { toDiscover.addLast(it) }
        }

        return discovered.filter { map[it.i][it.j] == '9' }
    }

    private fun isAdjacent(from: Position, to: Position, map: List<CharArray>): Boolean {
        return isOnMap(from, map) && isOnMap(to, map) && (map[to.i][to.j] - map[from.i][from.j] == 1)
    }

    private fun isOnMap(pos: Position, map: List<CharArray>): Boolean {
        return pos.i >= 0 && pos.j >= 0 && pos.i < map.size && pos.j < map.first().size
    }

    private fun readInput(input: List<String>): List<CharArray> = input.map { it.toCharArray() }

    data class Position(
        val i: Int,
        val j: Int,
    )

}
