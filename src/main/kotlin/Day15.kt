import util.CharMatrixCellTransformer
import util.Direction
import util.Matrix
import util.MatrixCell

fun main() = Day15().run()

class Day15 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val matrix = readMatrix(input)
        val movements = readMovements(input)
        var pos = matrix.allCellsWithValue('@').single()
        movements.forEach {
            pos = move(it, pos, matrix)
        }
        return matrix.allCellsWithValue('O').sumOf { it.i * 100 + it.j }.toString()
    }

    override fun part2(input: List<String>): String {
        val newMap = input
            .map { it.replace(".", "..") }
            .map { it.replace("@", "@.") }
            .map { it.replace("#", "##") }
            .map { it.replace("O", "[]") }

        var matrix = readMatrix(newMap)
        val movements = readMovements(input)

        movements.forEachIndexed { i, it ->
            val pos = matrix.allCellsWithValue('@').single()
            matrix = move2(it, pos, matrix)
        }
        println(matrix)
        return matrix.allCellsWithValue('[').sumOf { it.i * 100 + it.j }.toString()
    }

    private fun move2(
        move: Char,
        cell: MatrixCell<Char>,
        matrix: Matrix<Char>,
    ): Matrix<Char> {
        val direction = when (move) {
            '^' -> Direction.NORTH
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            'v' -> Direction.SOUTH
            else -> error("Invalid direction")
        }
        check(cell.value == '@') { "Cell is not the robot" }
        val newMatrix = matrix.copy()
        if (canMove2(direction, cell, matrix)) {
            move2Recursive(direction, cell, matrix, newMatrix)
        }
        newMatrix.cells().zipWithNext { a, b ->
            if (a.value == '[' && b.value != ']' || b.value == ']' && a.value != '[') {
                println(matrix)
                println("=".repeat(80))
                println(direction)
                println("=".repeat(80))
                println(newMatrix)
                error("")
            }
        }
        return newMatrix
    }

    private fun canMove2(direction: Direction, cell: MatrixCell<Char>, matrix: Matrix<Char>): Boolean {
        if (cell.value == '.') return true
        if (cell.value == '#') return false
        val next = when (Pair(direction, cell.value)) {
            Pair(Direction.NORTH, '[') -> listOf(
                matrix.adjacentOrNull(cell, direction)!!,
                matrix.adjacentOrNull(cell, Direction.NORTH_EAST)!!
            )

            Pair(Direction.NORTH, ']') -> listOf(
                matrix.adjacentOrNull(cell, direction)!!,
                matrix.adjacentOrNull(cell, Direction.NORTH_WEST)!!
            )

            Pair(Direction.SOUTH, '[') -> listOf(
                matrix.adjacentOrNull(cell, direction)!!,
                matrix.adjacentOrNull(cell, Direction.SOUTH_EAST)!!
            )

            Pair(Direction.SOUTH, ']') -> listOf(
                matrix.adjacentOrNull(cell, direction)!!,
                matrix.adjacentOrNull(cell, Direction.SOUTH_WEST)!!
            )

            else -> listOf(matrix.adjacentOrNull(cell, direction)!!)
        }
        return next.map {
            canMove2(direction, it, matrix)
        }.all { it }
    }

    private fun move2Recursive(
        direction: Direction,
        cell: MatrixCell<Char>,
        matrix: Matrix<Char>,
        newMatrix: Matrix<Char>
    ) {
        if (cell.value == '.') return
        if (cell.value == '#') error("Should not move this")
        if (cell.value == '@') {
            val next = matrix.adjacentOrNull(cell, direction)!!
            move2Recursive(direction, next, matrix, newMatrix)
            newMatrix.setValueAt(next.i, next.j, cell.value)
            newMatrix.setValueAt(cell.i, cell.j, '.')
        } else if (direction == Direction.EAST || direction == Direction.WEST) {
            val next = matrix.adjacentOrNull(cell, direction)!!
            move2Recursive(direction, next, matrix, newMatrix)
            newMatrix.setValueAt(next.i, next.j, cell.value)
            newMatrix.setValueAt(cell.i, cell.j, '.')
        } else {
            val nextCandidates = when (direction) {
                Direction.NORTH -> listOf(Direction.NORTH_EAST, Direction.NORTH, Direction.NORTH_WEST)
                Direction.SOUTH -> listOf(Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST)
                else -> error("")
            }
            nextCandidates.forEach {
                val next = newMatrix.adjacentOrNull(cell, it)!!
                if (next.value == cell.value) {
                    move2Recursive(direction, next, matrix, newMatrix)
                }
            }
            when (cell.value) {
                '[' -> listOf(cell, matrix.adjacentOrNull(cell, Direction.EAST)!!)
                ']' -> listOf(cell, matrix.adjacentOrNull(cell, Direction.WEST)!!)
                else -> error("")
            }.forEach {
                val next = matrix.adjacentOrNull(it, direction)!!
                newMatrix.setValueAt(next.i, next.j, it.value)
                newMatrix.setValueAt(it.i, it.j, '.')
            }
        }
    }

    private fun move(move: Char, cell: MatrixCell<Char>, matrix: Matrix<Char>): MatrixCell<Char> {
        val direction = when (move) {
            '^' -> Direction.NORTH
            '>' -> Direction.EAST
            '<' -> Direction.WEST
            'v' -> Direction.SOUTH
            else -> error("Invalid direction")
        }
        check(cell.value == '@') { "Cell is not the robot" }
        return if (moveNextRecursive(direction, cell, matrix)) {
            matrix.adjacentOrNull(cell, direction)!!
        } else {
            cell
        }
    }

    private fun moveNextRecursive(direction: Direction, cell: MatrixCell<Char>, matrix: Matrix<Char>): Boolean {
        val next = matrix.adjacentOrNull(cell, direction)
        if (next == null || next.value == '#') return false
        if (next.value == 'O' && !moveNextRecursive(direction, next, matrix)) return false
        matrix.setValueAt(next.i, next.j, cell.value)
        matrix.setValueAt(cell.i, cell.j, '.')
        return true
    }

    private fun readMatrix(input: List<String>) =
        input.filter { it.startsWith("#") }.let { Matrix.of(it, CharMatrixCellTransformer()) }

    private fun readMovements(input: List<String>) =
        input.filterNot { it.startsWith("#") || it.isBlank() }
            .flatMap { it.toCharArray().toList() }

}

