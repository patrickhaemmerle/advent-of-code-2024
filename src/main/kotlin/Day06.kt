fun main() = Day06().run()

class Day06 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val visitedPositions = findPath(input).map { it.first }.toSet()
        return visitedPositions.size.toString()
    }

    override fun part2(input: List<String>): String {
        val undistractedPath = findPath(input)
        val alreadySeenPositionAndDirection = mutableSetOf<Pair<Position, Direction>>()
        val alreadySeenPosition = mutableSetOf<Position>()
        var count = 0
        undistractedPath.zipWithNext { current, next ->
            val map = readInput(input)
            if (map[next.first.i][next.first.j] != '^') {
                map[next.first.i][next.first.j] = '#'
                if (!alreadySeenPosition.contains(next.first) && hasLoop(
                        map,
                        current.first,
                        current.second,
                        alreadySeenPositionAndDirection,
                    )
                ) count++
                alreadySeenPositionAndDirection.add(current)
                alreadySeenPosition.add(current.first)
            }
        }
        return count.toString()
    }

    private fun findPath(input: List<String>): List<Pair<Position, Direction>> {
        val map = readInput(input)
        var currentPosition = findStartingPosition(map)
        var currentDirection = Direction.NORTH
        val visitedPositions = mutableListOf<Pair<Position, Direction>>()
        while (currentPosition.i >= 0 && currentPosition.i < map.size && currentPosition.j >= 0 && currentPosition.j < map.first().size) {
            val nextPosition = Position(currentPosition.i + currentDirection.i, currentPosition.j + currentDirection.j)
            if (map.getOrNull(nextPosition.i)?.getOrNull(nextPosition.j) != '#') {
                visitedPositions.add(Pair(currentPosition, currentDirection))
                currentPosition = nextPosition
            } else {
                currentDirection = turn(currentDirection)
            }
        }
        return visitedPositions
    }

    private fun hasLoop(
        map: List<CharArray>,
        startingPosition: Position,
        startingDirection: Direction,
        alreadySeenPositionAndDirection: Set<Pair<Position, Direction>>,
    ): Boolean {
        var currentPosition = startingPosition
        var currentDirection = startingDirection
        val visitedPositions = alreadySeenPositionAndDirection.toMutableSet()
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