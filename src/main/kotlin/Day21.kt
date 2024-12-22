import util.CharMatrixCellTransformer
import util.Direction
import util.Matrix
import util.MatrixCell

fun main() = Day21().run()


class Day21 : AbstractDailyPuzzle() {

    val numericKeyPad = Matrix.of(
        listOf("789", "456", "123", ".0A"),
        CharMatrixCellTransformer()
    )

    val directionalKeyPad = Matrix.of(
        listOf(".^A", "<v>"),
        CharMatrixCellTransformer()
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
        val cascade = listOf(directionalKeyMap, directionalKeyMap, numericKeyMap)
        return input.sumOf { findCostForPathPressingEveryKey(it, cascade) * it.dropLast(1).toLong() }.toString()
    }

    override fun part2(input: List<String>): String {
        return "-"
    }

    private fun findCostForPathPressingEveryKey(path: String, cascade: List<Map<Pair<Char, Char>, Set<String>>>): Int {
        return ("A$path").zipWithNext { a, b -> findCostForTransitionAndPress(a, b, cascade) }.sum()
    }

    private fun findCostForTransitionAndPress(
        from: Char,
        to: Char,
        cascade: List<Map<Pair<Char, Char>, Set<String>>>
    ): Int {
        val result = findPathsForTransitionAndPressCascaded("$from$to", cascade, 0).minBy { it.length }.length
        return result
    }

    private fun findPathsForTransitionAndPressCascaded(
        code: String,
        cascade: List<Map<Pair<Char, Char>, Set<String>>>,
        depth: Int,
    ): Set<String> {
        if (depth >= cascade.size - 1) return findPathsForTransitionAndPress(code, cascade[depth])

        val result = findPathsForTransitionAndPressCascaded(code, cascade, depth + 1).flatMap {
            findPathsPressingEveryKey("A" + it, cascade[depth])
        }.groupBy { it.length }.minBy { it.key }.value.toSet()

        return result
    }

    private fun findPathsPressingEveryKey(
        code: String,
        pathMap: Map<Pair<Char, Char>, Set<String>>,
    ): Set<String> {
        if (code.length == 2) return pathMap[code[0] to code[1]]!!.map { it + "A" }.toSet()

        val result = findPathsPressingEveryKey(code.substring(0, code.length - 1), pathMap).flatMap { previous ->
            pathMap[code[code.length - 2] to code[code.length - 1]]!!.map { previous + it + "A" }
        }.groupBy { it.length }.minBy { it.key }.value.toSet()

        return result
    }

    private fun findPathsForTransitionAndPress(
        code: String,
        pathMap: Map<Pair<Char, Char>, Set<String>>,
    ): Set<String> {
        if (code.length == 2) return pathMap[code[0] to code[1]]!!.map { it + "A" }.toSet()

        val result =
            findPathsForTransitionAndPress(code.substring(0, code.length - 1), pathMap).flatMap { previous ->
                pathMap[code[code.length - 2] to code[code.length - 1]]!!.map { previous + it }
            }.groupBy { it.length }.minBy { it.key }.value.toSet()

        return result
    }

    private fun findAllShortestPathsForKeyPad(keyPad: Matrix<Char>): MutableMap<Pair<Char, Char>, Set<String>> {
        val keys = keyPad.cells().filter { it.value != '.' }
        val map = mutableMapOf<Pair<Char, Char>, Set<String>>()
        for (key1 in keys) {
            for (key2 in keys) {
                map[key1.value to key2.value] = findShortestPaths(key1, key2, keyPad)
            }
        }
        return map
    }

    private fun findShortestPaths(from: MatrixCell<Char>, to: MatrixCell<Char>, keyPad: Matrix<Char>): Set<String> {
        val visited = mutableMapOf<MatrixCell<Char>, BackTracking>()
        val toVisit = ArrayDeque<Pair<MatrixCell<Char>, BackTracking>>()

        toVisit.add(
            Pair(
                from,
                BackTracking(0, mutableSetOf(""))
            )
        )

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current.first in visited && current.second.cost > visited[current.first]!!.cost) {
                continue
            }
            if (current.first != to) {
                directionToCommand.forEach { direction ->
                    keyPad.allAdjacentCells(current.first, setOf(direction.key)).filter { it.value != '.' }
                        .forEach {
                            toVisit.add(
                                Pair(
                                    it,
                                    BackTracking(
                                        current.second.cost + 1,
                                        current.second.paths.map { it + directionToCommand[direction.key] }
                                            .toMutableSet()
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
        return visited[to]?.paths ?: emptySet()
    }
}

private data class BackTracking(
    val cost: Int,
    val paths: MutableSet<String>
)