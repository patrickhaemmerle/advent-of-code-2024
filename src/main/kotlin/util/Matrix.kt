package util

class Matrix<T> private constructor(
    private val matrix: Array<Array<MatrixCell<T>>>,
) {
    val width = matrix.firstOrNull()?.size ?: 0
    val height = matrix.size

    fun cells() = matrix.flatten()

    fun cellAt(i: Int, j: Int) = matrix[i][j]

    fun cellAtOrNull(i: Int, j: Int) = if (isOnMatrix(i, j)) cellAt(i, j) else null

    fun isOnMatrix(i: Int, j: Int) = i >= 0 && j >= 0 && i < height && j < width

    fun allCellsWithValue(value: T) = matrix.flatMap { cells -> cells.filter { it.value == value } }

    fun adjacentOrNull(from: MatrixCell<T>, direction: Direction) =
        cellAtOrNull(from.i + direction.i, from.j + direction.j)

    fun allAdjacentCells(from: MatrixCell<T>, directions: Set<Direction>): List<MatrixCell<T>> =
        directions.mapNotNull { adjacentOrNull(from, it) }

    companion object {
        fun <T> of(
            input: List<String>,
            matrixCellInitializer: MatrixCellInitializer<T>,
        ): Matrix<T> {
            check(input.map { it.length }.distinct().size == 1) { "Not all lines in the input have the same length" }
            val matrix = input.mapIndexed { i, line ->
                line.toCharArray().mapIndexed { j, char ->
                    MatrixCell(i, j, matrixCellInitializer.toCellValue(char))
                }.toTypedArray()
            }.toTypedArray()
            return Matrix(matrix)
        }
    }
}

class MatrixCell<T>(
    val i: Int,
    val j: Int,
    val value: T,
)

interface MatrixCellInitializer<T> {
    fun toCellValue(char: Char): T
}

class CharMatrixCellInitializer : MatrixCellInitializer<Char> {
    override fun toCellValue(char: Char): Char = char
}

class IntMatrixCellInitializer : MatrixCellInitializer<Int> {
    override fun toCellValue(char: Char): Int = char.digitToInt()
}


