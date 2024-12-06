fun main() = Day6().run()

class Day6 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val map = readInput(input)
        var currentPosition = findStartingPosition(map)
        var currentDirection = Direction.NORTH
        val visitedPositions = mutableSetOf<Position>()
        while (currentPosition.i >= 0 && currentPosition.i < map.size && currentPosition.j >= 0 && currentPosition.j < map.first().size) {
            val nextPosition = Position(currentPosition.i + currentDirection.i, currentPosition.j + currentDirection.j)
            if (map.getOrNull(nextPosition.i)?.getOrNull(nextPosition.j) != '#') {
                visitedPositions.add(currentPosition)
                currentPosition = nextPosition
            } else {
                currentDirection = turn(currentDirection)
            }
        }
        return visitedPositions.size.toString()
    }

    override fun part2(input: List<String>): String {
        var count = 0
        for (i in input.indices) {
            for (j in input.first().indices) {
                val map = readInput(input)
                if (map[i][j] != '^') {
                    map[i][j] = '#'
                    if (hasLoop(map)) count++
                }
            }
        }
        return count.toString()
    }

    private fun hasLoop(map: List<CharArray>): Boolean {
        var currentPosition = findStartingPosition(map)
        var currentDirection = Direction.NORTH
        val visitedPositions = mutableSetOf<Pair<Position, Direction>>()
        while (currentPosition.i >= 0 && currentPosition.i < map.size && currentPosition.j >= 0 && currentPosition.j < map.first().size) {
            val nextPosition = Position(currentPosition.i + currentDirection.i, currentPosition.j + currentDirection.j)
            if (map.getOrNull(nextPosition.i)?.getOrNull(nextPosition.j) != '#') {
                if (Pair(currentPosition, currentDirection) in visitedPositions) {
                    return true
                }
                visitedPositions.add(Pair(currentPosition, currentDirection))
                currentPosition = nextPosition
            } else {
                currentDirection = turn(currentDirection)
            }
        }
        return false
    }

    private fun turn(current: Direction) = when (current) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }

    private fun findStartingPosition(
        map: List<CharArray>,
    ): Position {
        for (i in map.indices) {
            for (j in map.first().indices) {
                if (map[i][j] == '^') {
                    return Position(i, j)
                }
            }
        }
        error("There is no one")
    }

    private fun readInput(input: List<String>) = input.map { it.toCharArray() }

}

data class Position(
    val i: Int,
    val j: Int,
)

enum class Direction(
    val i: Int,
    val j: Int
) {
    NORTH(-1, 0),
    EAST(0, 1),
    WEST(0, -1),
    SOUTH(1, 0),
    ;
}