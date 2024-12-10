package util

import util.Direction.*

val DIRECTION_ORTHOGONALS = setOf(NORTH, WEST, SOUTH, EAST)
val DIRECTION_DIAGONALS = setOf(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST)
val DIRECTION_ALL = Direction.entries.toSet()

enum class Direction(val i: Int, val j: Int) {
    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1);
}