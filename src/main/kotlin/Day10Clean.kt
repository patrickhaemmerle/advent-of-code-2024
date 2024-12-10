import util.maze.*

fun main() = Day10Clean().run()

class Day10Clean : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val maze = readMaze(input)

        return maze.allCellsWithValue(0).flatMap { head ->
            maze.allCellsWithValue(9).map { peak ->
                maze.findShortestPath(head, peak)
            }.filter { it.isNotEmpty() }
        }.count().toString()
    }

    override fun part2(input: List<String>): String {
        val maze = readMaze(input)

        return maze.allCellsWithValue(0).flatMap { head ->
            maze.allCellsWithValue(9).flatMap { peak ->
                maze.findAllPaths(head, peak)
            }.filter { it.isNotEmpty() }
        }.count().toString()
    }

    private fun readMaze(input: List<String>): Maze<Int> {
        val maze = Maze.of(
            input = input,
            mazeCellInitializer = IntMazeCellInitializer(),
            allowedDirections = DIRECTION_ORTHOGONALS
        ) { from, to -> to.value - from.value == 1 }
        return maze
    }
}