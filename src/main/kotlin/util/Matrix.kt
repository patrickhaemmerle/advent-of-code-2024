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

    fun updateValueAt(i: Int, j: Int, updateFunction: (old: T) -> T) {
        checkOnMatrix(i, j)
        matrix[i][j] = MatrixCell(i, j, updateFunction(cellAt(i, j).value))
    }

    fun setValueAt(i: Int, j: Int, newValue: T): MatrixCell<T> {
        checkOnMatrix(i, j)
        val newCell = MatrixCell(i, j, newValue)
        matrix[i][j] = newCell
        return newCell
    }

    fun copy(): Matrix<T> = Matrix(matrix.map { it.clone() }.toTypedArray())

    override fun toString(): String {
        return matrix.joinToString("\n") { row -> row.joinToString("") { it.toValueString() } }
    }

    private fun checkOnMatrix(i: Int, j: Int) = check(isOnMatrix(i, j)) { "($i,$j) is not on the matrix" }

    companion object {
        fun <T> of(
            input: List<String>,
            matrixCellTransformer: MatrixCellTransformer<T>,
        ): Matrix<T> {
            check(input.map { it.length }.distinct().size <= 1) { "Not all lines in the input have the same length" }
            val matrix = input.mapIndexed { i, line ->
                line.toCharArray().mapIndexed { j, char ->
                    MatrixCell(i, j, matrixCellTransformer.toCellValue(char))
                }.toTypedArray()
            }.toTypedArray()
            return Matrix(matrix)
        }

        fun <T> emptyMatrix(height: Int, width: Int, initialValue: T): Matrix<T> {
            val matrix = Matrix(Array(height) { Array(width) { MatrixCell(-1, -1, initialValue) } })
            for (i in 0 until matrix.height) for (j in 0 until matrix.width) matrix.setValueAt(i, j, initialValue)
            return matrix
        }
    }
}

class MatrixCell<T>(
    val i: Int,
    val j: Int,
    val value: T,
) {
    fun toValueString(): String {
        return value.toString()
    }

    override fun toString(): String {
        return "($i, $j) $value"
    }
}

interface MatrixCellTransformer<T> {
    fun toCellValue(char: Char): T
}

class CharMatrixCellTransformer : MatrixCellTransformer<Char> {
    override fun toCellValue(char: Char): Char = char
}

class IntMatrixCellTransformer : MatrixCellTransformer<Int> {
    override fun toCellValue(char: Char): Int = char.digitToInt()
}


