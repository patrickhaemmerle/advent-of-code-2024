import util.*

fun main() = Day21Clean().run()


class Day21Clean : AbstractDailyPuzzle() {

    val numericKeyPad = Matrix.of(
        listOf("789", "456", "123", ".0A"), CharMatrixCellTransformer()
    )

    val directionalKeyPad = Matrix.of(
        listOf(".^A", "<v>"), CharMatrixCellTransformer()
    )

    val directionToCommand = mapOf(
        Direction.NORTH to '^',
        Direction.WEST to '<',
        Direction.SOUTH to 'v',
        Direction.EAST to '>',
    )

    val numericKeyMap = findAllShortestPathsForKeyPad(numericKeyPad)
    val directionalKeyMap = findAllShortestPathsForKeyPad(directionalKeyPad)

    override fun part1(input: List<String>): String {
        return input
            .sumOf { costForCode(it, 2, numericKeyMap) * it.dropLast(1).toLong() }
            .toString()

    }

    override fun part2(input: List<String>): String {
        return input
            .sumOf { costForCode(it, 25, numericKeyMap) * it.dropLast(1).toLong() }
            .toString()
    }

    private val cache = mutableMapOf<Pair<String, Int>, Long>()
    private fun costForCode(
        code: String,
        depth: Int,
        keyMap: Map<Pair<Char, Char>, List<String>>
    ): Long {
        val cacheKey = code to depth
        if (cacheKey in cache) return cache[cacheKey]!!

        val result = "A$code"
            .zipWithNext { a, b ->
                if (depth == 0) {
                    keyMap[a to b]!!.map { it + "A" }.minOf { it.length }.toLong()
                } else {
                    keyMap[a to b]!!.map { it + "A" }.minOf { costForCode(it, depth - 1, directionalKeyMap) }
                }
            }.sum()

        cache[cacheKey] = result
        return result
    }

    private fun findAllShortestPathsForKeyPad(keyPad: Matrix<Char>): MutableMap<Pair<Char, Char>, List<String>> {
        val keys = keyPad.cells().filter { it.value != '.' }
        val map = mutableMapOf<Pair<Char, Char>, List<String>>()
        for (key1 in keys) {
            for (key2 in keys) {
                map[key1.value to key2.value] = findShortestPaths(key1, key2, keyPad)
            }
        }
        return map
    }

    private fun findShortestPaths(from: MatrixCell<Char>, to: MatrixCell<Char>, keyPad: Matrix<Char>): List<String> {
        val visited = mutableMapOf<MatrixCell<Char>, BackTracking>()
        val toVisit = ArrayDeque<Pair<MatrixCell<Char>, BackTracking>>()

        toVisit.add(
            Pair(
                from, BackTracking(0, mutableSetOf(""))
            )
        )

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current.first in visited && current.second.cost > visited[current.first]!!.cost) {
                continue
            }
            if (current.first != to) {
                directionToCommand.forEach { direction ->
                    keyPad.allAdjacentCells(current.first, setOf(direction.key)).filter { it.value != '.' }.forEach {
                        toVisit.add(
                            Pair(
                                it, BackTracking(
                                    current.second.cost + 1,
                                    current.second.paths.map { it + directionToCommand[direction.key] }.toMutableSet()
                                )
                            )
                        )
                    }
                }
            }
            if (current.first in visited && current.second.cost == visited[current.first]!!.cost) {
                visited[current.first]!!.paths.addAll(current.second.paths)
            } else {
                visited[current.first] = current.second
            }
        }
        return visited[to]?.paths?.toList() ?: emptyList()
    }

    private data class BackTracking(
        val cost: Int, val paths: MutableSet<String>
    )
}