import util.CharMatrixCellTransformer
import util.DIRECTION_ORTHOGONALS
import util.Matrix
import util.MatrixCell
import kotlin.math.absoluteValue

fun main() = Day20().run()

class Day20 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val matrix = readInput(input)
        val start = matrix.allCellsWithValue('S').single()
        val minimumSavings = if (input.size <= 15) 2 else 100
        val visited = findPath(start, matrix)

        val result = visited.keys
            .map { cell ->
                cell to matrix.allAdjacentCells(cell, DIRECTION_ORTHOGONALS).filter { it.value == '#' }
            }
            .map { candidate ->
                candidate.second.flatMap {
                    matrix.allAdjacentCells(it, DIRECTION_ORTHOGONALS)
                        .filter { it != candidate.first }
                        .filter { it in visited }
                        .filter { (visited[it] ?: 0) - minimumSavings > (visited[candidate.first] ?: Int.MAX_VALUE) }
                }.count()
            }.sum()

        return result.toString()
    }

    override fun part2(input: List<String>): String {
        val matrix = readInput(input)
        val start = matrix.allCellsWithValue('S').single()
        val minimumSavings = if (input.size <= 15) 72 else 100
        val visited = findPath(start, matrix)

        val cheatSet = mutableSetOf<Pair<MatrixCell<Char>, MatrixCell<Char>>>()
        for (cheatStart in visited.entries) {
            for (i in 0..20) {
                for (j in 0..20 - i) {
                    listOfNotNull(
                        matrix.cellAtOrNull(cheatStart.key.i + i, cheatStart.key.j + j),
                        matrix.cellAtOrNull(cheatStart.key.i - i, cheatStart.key.j + j),
                        matrix.cellAtOrNull(cheatStart.key.i + i, cheatStart.key.j - j),
                        matrix.cellAtOrNull(cheatStart.key.i - i, cheatStart.key.j - j),
                    ).filter { it.value != '#' }
                        .filter { visited[it]!! - cheatStart.value - (i.absoluteValue + j.absoluteValue) >= minimumSavings }
                        .forEach { cheatSet.add(cheatStart.key to it) }
                }
            }
        }
        return cheatSet.size.toString()
    }

    private fun findPath(
        start: MatrixCell<Char>,
        matrix: Matrix<Char>
    ): MutableMap<MatrixCell<Char>, Int> {
        val visited = mutableMapOf<MatrixCell<Char>, Int>()
        val toVisit = ArrayDeque<Pair<MatrixCell<Char>, Int>>()
        toVisit.add(Pair(start, 0))

        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if ((visited[current.first] ?: Int.MAX_VALUE) < current.second) continue

            if (current.first.value != 'E') {
                matrix.allAdjacentCells(current.first, DIRECTION_ORTHOGONALS)
                    .filter { it.value != '#' }
                    .forEach { toVisit.add(Pair(it, current.second + 1)) }
            }
            visited[current.first] = current.second
        }
        return visited
    }

    private fun readInput(input: List<String>) = Matrix.of(input, CharMatrixCellTransformer())
}