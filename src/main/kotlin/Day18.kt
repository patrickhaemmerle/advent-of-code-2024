import util.CharMatrixCellTransformer
import util.DIRECTION_ORTHOGONALS
import util.Matrix
import util.MatrixCell

fun main() = Day18().run()

class Day18 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val steps = if (input.size <= 25) 12 else 1024
        val matrixSize = if (input.size <= 25) 7 else 71
        val fallenBytes = readInput(input)
        val matrixInput = (0 until matrixSize).map { ".".repeat(matrixSize) }
        val matrix = Matrix.of(matrixInput, CharMatrixCellTransformer())
        for (i in 0 until steps) {
            matrix.setValueAt(fallenBytes[i].first, fallenBytes[i].second, '#')
        }
        val seen = mutableMapOf<MatrixCell<Char>, Int>()
        val visitNext = ArrayDeque<Pair<MatrixCell<Char>, Int>>()
        visitNext.add(Pair(matrix.cellAt(0, 0), 0))
        while (visitNext.isNotEmpty()) {
            val current = visitNext.removeFirst()
            if (current.first in seen && seen[current.first]!! <= current.second) continue
            matrix.allAdjacentCells(current.first, DIRECTION_ORTHOGONALS).forEach {
                if (it.value != '#') visitNext.add(Pair(it, current.second + 1))
            }
            seen[current.first] = current.second
        }
        val shortestPath = seen[matrix.cellAt(matrixSize - 1, matrixSize - 1)]
        return shortestPath?.toString() ?: "No Path"
    }

    override fun part2(input: List<String>): String {
        val steps = if (input.size <= 25) 12 else 1024
        val matrixSize = if (input.size <= 25) 7 else 71
        val fallenBytes = readInput(input)
        val matrixInput = (0 until matrixSize).map { ".".repeat(matrixSize) }
        val matrix = Matrix.of(matrixInput, CharMatrixCellTransformer())
        for (i in 0 until steps) {
            matrix.setValueAt(fallenBytes[i].first, fallenBytes[i].second, '#')
        }
        for (i in steps until fallenBytes.size) {
            matrix.setValueAt(fallenBytes[i].first, fallenBytes[i].second, '#')
            if (findShortestPath(matrix, matrixSize) == null) {
                return "${fallenBytes[i].second},${fallenBytes[i].first}"
            }
        }
        error("This is not expected, but it would be gread... we'd have no hurry.")
    }

    private fun findShortestPath(matrix: Matrix<Char>, matrixSize: Int): Int? {
        val seen = mutableMapOf<MatrixCell<Char>, Int>()
        val visitNext = ArrayDeque<Pair<MatrixCell<Char>, Int>>()
        visitNext.add(Pair(matrix.cellAt(0, 0), 0))
        while (visitNext.isNotEmpty()) {
            val current = visitNext.removeFirst()
            if (current.first in seen && seen[current.first]!! <= current.second) continue
            matrix.allAdjacentCells(current.first, DIRECTION_ORTHOGONALS).forEach {
                if (it.value != '#') visitNext.add(Pair(it, current.second + 1))
            }
            seen[current.first] = current.second
        }
        val shortestPath = seen[matrix.cellAt(matrixSize - 1, matrixSize - 1)]
        return shortestPath
    }

    private fun readInput(input: List<String>): List<Pair<Int, Int>> =
        input.map {
            val coordinates = it.split(",").map { it.trim().toInt() }
            Pair(coordinates[1], coordinates[0])
        }
}