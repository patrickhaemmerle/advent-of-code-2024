import util.DIRECTION_ORTHOGONALS
import util.Direction

fun main() = Day12().run()

class Day12 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val grid = readInput(input)
        return findSolution(grid).first.toString()
    }

    override fun part2(input: List<String>): String {
        val grid = readInput(input)
        return findSolution(grid).second.toString()
    }

    private fun findSolution(grid: Array<Array<Plot>>): Pair<Long, Long> {
        val seen = mutableSetOf<Plot>()
        val discoverNext = ArrayDeque<Plot>()
        val discoverLater = ArrayDeque<Plot>()
        var totalCost = 0L
        var totalCostDiscounted = 0L

        discoverLater.add(grid[0][0])
        while (discoverLater.isNotEmpty()) {
            val currentOnGrid = discoverLater.removeFirst()
            if (currentOnGrid in seen) continue
            discoverNext.add(currentOnGrid)

            var currentFenceLength = 0L
            var currentNumberOfSides = 0L
            var currentArea = 0L
            while (discoverNext.isNotEmpty()) {
                val current = discoverNext.removeFirst()
                if (current in seen) continue
                seen.add(current)
                val neighbourCount = DIRECTION_ORTHOGONALS.mapNotNull {
                    if (current.i + it.i >= 0 && current.j + it.j >= 0 && current.i + it.i < grid.size && current.j + it.j < grid.first().size) {
                        val neighbour = grid[current.i + it.i][current.j + it.j]
                        if (neighbour.value == current.value) {
                            discoverNext.add(neighbour)
                        } else {
                            discoverLater.add(neighbour)
                            currentFenceLength++
                        }
                        return@mapNotNull neighbour
                    }
                    null
                }.count()
                currentFenceLength += 4 - neighbourCount
                currentNumberOfSides += countCorners(current, grid)
                currentArea++
            }
            totalCost += currentFenceLength * currentArea
            totalCostDiscounted += currentNumberOfSides * currentArea
        }
        return Pair(totalCost, totalCostDiscounted)
    }

    private fun countCorners(plot: Plot, grid: Array<Array<Plot>>): Int {
        return listOf(
            isInnerCorner(plot, Triple(Direction.NORTH_EAST, Direction.NORTH, Direction.EAST), grid),
            isInnerCorner(plot, Triple(Direction.SOUTH_EAST, Direction.SOUTH, Direction.EAST), grid),
            isInnerCorner(plot, Triple(Direction.NORTH_WEST, Direction.NORTH, Direction.WEST), grid),
            isInnerCorner(plot, Triple(Direction.SOUTH_WEST, Direction.SOUTH, Direction.WEST), grid),
            isOuterCorner(plot, Pair(Direction.NORTH, Direction.EAST), grid),
            isOuterCorner(plot, Pair(Direction.EAST, Direction.SOUTH), grid),
            isOuterCorner(plot, Pair(Direction.SOUTH, Direction.WEST), grid),
            isOuterCorner(plot, Pair(Direction.WEST, Direction.NORTH), grid),
        ).count { it }
    }

    private fun isOuterCorner(
        plot: Plot,
        dirs: Pair<Direction, Direction>,
        grid: Array<Array<Plot>>
    ): Boolean {
        return ((!isOnGrid(plot.i + dirs.first.i, plot.j + dirs.first.j, grid) ||
                grid[plot.i + dirs.first.i][plot.j + dirs.first.j].value != plot.value) &&
                (!isOnGrid(plot.i + dirs.second.i, plot.j + dirs.second.j, grid) ||
                        grid[plot.i + dirs.second.i][plot.j + dirs.second.j].value != plot.value))
    }

    private fun isInnerCorner(
        plot: Plot,
        dirs: Triple<Direction, Direction, Direction>,
        grid: Array<Array<Plot>>
    ): Boolean {
        if (dirs.toList().any { !isOnGrid(plot.i + it.i, plot.j + it.j, grid) }) return false
        return (grid[plot.i + dirs.first.i][plot.j + dirs.first.j].value != plot.value &&
                grid[plot.i + dirs.second.i][plot.j + dirs.second.j].value == plot.value &&
                grid[plot.i + dirs.third.i][plot.j + dirs.third.j].value == plot.value
                )
    }

    private fun isOnGrid(i: Int, j: Int, grid: Array<Array<Plot>>): Boolean {
        return !(i < 0 || j < 0 || i >= grid.size || j >= grid.first().size)
    }

    private fun readInput(input: List<String>): Array<Array<Plot>> = input.mapIndexed { i, line ->
        line.toCharArray().mapIndexed { j, value ->
            Plot(i, j, value)
        }.toTypedArray()
    }.toTypedArray()

    class Plot(
        val i: Int,
        val j: Int,
        val value: Char,
    ) {
        override fun toString(): String = "Plot($i, $j) - $value"
    }

}