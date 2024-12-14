import util.*

fun main() = Day12WithMatrix().run()

class Day12WithMatrix : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val matrix = readInput(input)
        return calculateCost(matrix).toString()
    }

    override fun part2(input: List<String>): String {
        val matrix = readInput(input)
        return calculateCost(matrix, true).toString()
    }

    private fun calculateCost(matrix: Matrix<Char>, discount: Boolean = false): Long {
        val seen = mutableSetOf<MatrixCell<Char>>()
        val toVisit = ArrayDeque<MatrixCell<Char>>()

        var cost = 0L
        matrix.cells().forEach { cell ->
            toVisit.addLast(cell)
            var area = 0L
            var fenceLength = 0L
            var fenceSegments = 0L
            while (toVisit.isNotEmpty()) {
                val current = toVisit.removeFirst()
                if (current in seen) continue

                val neighboursInSameRegion = matrix.allAdjacentCells(
                    current,
                    DIRECTION_ORTHOGONALS
                ).filter { it.value == current.value }

                area++
                fenceLength += 4 - neighboursInSameRegion.size
                fenceSegments += countCorners(current, matrix)

                toVisit.addAll(neighboursInSameRegion)
                seen.add(current)
            }
            cost += if (discount) area * fenceSegments else area * fenceLength
        }
        return cost
    }

    private fun countCorners(current: MatrixCell<Char>, matrix: Matrix<Char>): Long = listOf(
        isInnerCorner(current, matrix, Direction.NORTH, Direction.EAST, Direction.NORTH_EAST),
        isInnerCorner(current, matrix, Direction.NORTH, Direction.WEST, Direction.NORTH_WEST),
        isInnerCorner(current, matrix, Direction.SOUTH, Direction.EAST, Direction.SOUTH_EAST),
        isInnerCorner(current, matrix, Direction.SOUTH, Direction.WEST, Direction.SOUTH_WEST),
        isOuterCorner(current, matrix, Direction.NORTH, Direction.EAST),
        isOuterCorner(current, matrix, Direction.EAST, Direction.SOUTH),
        isOuterCorner(current, matrix, Direction.SOUTH, Direction.WEST),
        isOuterCorner(current, matrix, Direction.WEST, Direction.NORTH),
    ).count { it }.toLong()

    private fun isInnerCorner(
        cell: MatrixCell<Char>,
        matrix: Matrix<Char>,
        d1: Direction,
        d2: Direction,
        d3: Direction
    ): Boolean =
        matrix.adjacentOrNull(cell, d1)?.value == cell.value &&
                matrix.adjacentOrNull(cell, d2)?.value == cell.value &&
                matrix.adjacentOrNull(cell, d3)?.value != cell.value

    private fun isOuterCorner(
        cell: MatrixCell<Char>,
        matrix: Matrix<Char>,
        d1: Direction,
        d2: Direction,
    ): Boolean =
        matrix.adjacentOrNull(cell, d1)?.value != cell.value && matrix.adjacentOrNull(cell, d2)?.value != cell.value

    private fun readInput(input: List<String>): Matrix<Char> =
        Matrix.of(input, CharMatrixCellInitializer())

}