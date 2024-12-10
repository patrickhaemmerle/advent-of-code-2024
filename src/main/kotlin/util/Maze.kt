package util

class Maze<T> private constructor(
    private val matrix: List<MutableList<MazeCell<T>>>,
    private val allowedDirections: Set<Direction>,
    private val isStepAllowed: (from: MazeCell<T>, to: MazeCell<T>) -> Boolean
) {
    val width = matrix.firstOrNull()?.size ?: 0
    val height = matrix.size

    fun isOnMaze(i: Int, j: Int) = i >= 0 && j >= 0 && i < height && j < width
    fun cellAt(i: Int, j: Int) = matrix[i][j]
    fun allCellsWithValue(value: T) = matrix.flatMap { cells -> cells.filter { it.value == value } }

    fun adjacentOrNull(from: MazeCell<T>, direction: Direction): MazeCell<T>? {
        val i = from.i + direction.i
        val j = from.j + direction.j
        return if (direction in allowedDirections && isOnMaze(i, j)) {
            val to = cellAt(i, j)
            if (isStepAllowed(from, to)) to else null
        } else null
    }

    fun allAdjacentCells(from: MazeCell<T>): List<MazeCell<T>> = allowedDirections
        .mapNotNull { adjacentOrNull(from, it) }
        .filter { isStepAllowed(from, it) }

    fun findShortestPath(from: MazeCell<T>, to: MazeCell<T>): List<MazeCell<T>> {
        data class MazeCellWithPrevious<T>(
            val mazeCell: MazeCell<T>,
            val previous: MazeCellWithPrevious<T>?
        )

        val seen = mutableSetOf<MazeCell<T>>()
        val toBeVisited = ArrayDeque<MazeCellWithPrevious<T>>()
        toBeVisited.add(MazeCellWithPrevious(from, null))

        var last: MazeCellWithPrevious<T>? = null
        while (toBeVisited.isNotEmpty()) {
            val current = toBeVisited.removeFirst()

            if (current.mazeCell == to) {
                last = current
                break
            }

            seen.add(current.mazeCell)
            val nextNodes = allAdjacentCells(current.mazeCell)
                .filterNot { it in seen }
                .map { MazeCellWithPrevious(it, current) }
            toBeVisited.addAll(nextNodes)
        }

        val path = mutableListOf<MazeCell<T>>()
        while (last != null) {
            path.add(last.mazeCell)
            last = last.previous
        }
        return path.asReversed()
    }

    fun findAllPaths(from: MazeCell<T>, to: MazeCell<T>): List<List<MazeCell<T>>> {
        data class MazeCellWithPrevious<T>(
            val mazeCell: MazeCell<T>,
            val previous: MazeCellWithPrevious<T>?
        )

        val toBeVisited = ArrayDeque<MazeCellWithPrevious<T>>()
        toBeVisited.add(MazeCellWithPrevious(from, null))

        val pathTails = mutableListOf<MazeCellWithPrevious<T>>()
        while (toBeVisited.isNotEmpty()) {
            val current = toBeVisited.removeFirst()

            if (current.mazeCell == to) {
                pathTails.add(current)
                continue
            }

            val nextNodes = allAdjacentCells(current.mazeCell)
                .map { MazeCellWithPrevious(it, current) }
            toBeVisited.addAll(nextNodes)
        }

        return pathTails.map {
            val path = mutableListOf<MazeCell<T>>()
            var last: MazeCellWithPrevious<T>? = it
            while (last != null) {
                path.add(last.mazeCell)
                last = last.previous
            }
            path.asReversed()
        }
    }

    companion object {
        fun <T> of(
            input: List<String>,
            mazeCellInitializer: MazeCellInitializer<T>,
            allowedDirections: Set<Direction>,
            isStepAllowed: (from: MazeCell<T>, to: MazeCell<T>) -> Boolean,
        ): Maze<T> {
            val matrix = input.mapIndexed { i, line ->
                line.toCharArray().mapIndexed { j, char ->
                    MazeCell(i, j, mazeCellInitializer.toCellValue(char))
                }.toMutableList()
            }
            return Maze(matrix, allowedDirections, isStepAllowed)
        }
    }
}

class MazeCell<T>(
    val i: Int,
    val j: Int,
    val value: T
)

interface MazeCellInitializer<T> {
    fun toCellValue(char: Char): T
}

class CharMazeCellInitializer : MazeCellInitializer<Char> {
    override fun toCellValue(char: Char): Char = char
}

class IntMazeCellInitializer : MazeCellInitializer<Int> {
    override fun toCellValue(char: Char): Int = char.digitToInt()
}


