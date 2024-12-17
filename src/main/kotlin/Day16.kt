import util.*

fun main() = Day16().run()

class Day16 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val maze = readInput(input)
        val startDirection = Direction.EAST
        val start = maze.allCellsWithValue('S').single()

        val toVisit = ArrayDeque<NodeToVisit>()
        val visited = mutableMapOf<Node, BacktrackingData>()
        toVisit.add(
            NodeToVisit(
                Node(start, startDirection),
                BacktrackingData(0L, null)
            )
        )
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current.node in visited && current.backtrackingData.cost >= visited[current.node]!!.cost) continue

            visited[current.node] = current.backtrackingData

            // We can do three different turns
            DIRECTION_ORTHOGONALS
                .filterNot { it == current.node.direction }
                .forEach {
                    toVisit.add(
                        NodeToVisit(
                            Node(current.node.cell, it),
                            BacktrackingData(
                                current.backtrackingData.cost + turningCost(current.node.direction, it),
                                current.node
                            )
                        )
                    )
                }

            // Or we can go straight
            val next = maze.adjacentOrNull(current.node.cell, current.node.direction)!!
            if (next.value != '#') {
                toVisit.add(
                    NodeToVisit(
                        Node(next, current.node.direction),
                        BacktrackingData(current.backtrackingData.cost + 1, current.node)
                    )
                )
            }
        }

        return visited
            .filter { it.key.cell.value == 'E' }
            .minOf { it.value.cost }
            .toString()
    }

    /**
     * This still has a bug, that luckily only shows up on the test data but not on the acutal input ;-)
     */
    override fun part2(input: List<String>): String {
        val maze = readInput(input)
        val startDirection = Direction.EAST
        val start = maze.allCellsWithValue('S').single()

        val toVisit = ArrayDeque<NodeToVisit>()
        val visited = mutableMapOf<Node, BacktrackingData>()
        val allPathsList = mutableMapOf<Node, MutableList<BacktrackingData>>()
        toVisit.add(
            NodeToVisit(
                Node(start, startDirection),
                BacktrackingData(0L, null)
            )
        )
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            if (current.node in visited) {
                if (current.backtrackingData.cost > visited[current.node]!!.cost) {
                    continue
                } else if (current.backtrackingData.cost == visited[current.node]!!.cost) {
                    allPathsList.compute(current.node) { _, v ->
                        (v ?: mutableListOf()).apply { add(current.backtrackingData) }
                    }
                    continue
                }
            }
            visited[current.node] = current.backtrackingData
            allPathsList[current.node] = mutableListOf(current.backtrackingData)

            // We can do three different turns
            DIRECTION_ORTHOGONALS
                .filterNot { it == current.node.direction }
                .forEach {
                    toVisit.add(
                        NodeToVisit(
                            Node(current.node.cell, it),
                            BacktrackingData(
                                current.backtrackingData.cost + turningCost(current.node.direction, it),
                                current.node
                            )
                        )
                    )
                }

            // Or we can go straight
            val next = maze.adjacentOrNull(current.node.cell, current.node.direction)!!
            if (next.value != '#') {
                toVisit.add(
                    NodeToVisit(
                        Node(next, current.node.direction),
                        BacktrackingData(current.backtrackingData.cost + 1, current.node)
                    )
                )
            }
        }

        val end = maze.allCellsWithValue('E').single()
        val nodesToBacktrack = ArrayDeque<Node>()
        val bestSpots = mutableSetOf<MatrixCell<Char>>()
        nodesToBacktrack.addAll(DIRECTION_ORTHOGONALS.map { Node(end, it) })

        while (nodesToBacktrack.isNotEmpty()) {
            val current = nodesToBacktrack.removeFirst()
            bestSpots.add(current.cell)

            allPathsList[current]
                ?.mapNotNull { it.from }
                ?.forEach { nodesToBacktrack.add(it) }
        }

        bestSpots.forEach {
            maze.setValueAt(it.i, it.j, 'O')
        }
//        println(maze)

        return bestSpots.count().toString()
    }

    private fun turningCost(from: Direction, to: Direction): Long {
        val turn = setOf(from, to)
        return if (Direction.NORTH in turn && Direction.SOUTH in turn) {
            2000L
        } else if (Direction.WEST in turn && Direction.EAST in turn) {
            2000L
        } else if (from == to) {
            0L
        } else {
            1000L
        }
    }

    private fun readInput(input: List<String>) = Matrix.of(input, CharMatrixCellTransformer())
}

data class Node(
    val cell: MatrixCell<Char>,
    val direction: Direction,
)

data class BacktrackingData(
    val cost: Long,
    val from: Node?,
)

data class NodeToVisit(
    val node: Node,
    val backtrackingData: BacktrackingData,
)