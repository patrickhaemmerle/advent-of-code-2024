import util.CharMatrixCellTransformer
import util.Matrix
import kotlin.math.absoluteValue

fun main() = Day21().run()


class Day21 : AbstractDailyPuzzle() {

    val numericKeyPad = Matrix.of(
        listOf("789", "456", "123", ".0A"),
        CharMatrixCellTransformer()
    )

    val directionalKeyPad = Matrix.of(
        listOf(".^A", "<v>"),
        CharMatrixCellTransformer()
    )

    val resolveCodeCache = mutableMapOf<Pair<String, Matrix<Char>>, List<String>>()
    val resolveMultilevelCodeCache = mutableMapOf<Triple<String, List<Matrix<Char>>, Int>, List<String>>()

    override fun part1(input: List<String>): String {
        val cascade = listOf(directionalKeyPad, directionalKeyPad, numericKeyPad)
        resolveMutlilevelCode("029A", cascade, 0)
            .filter { it == "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A" }
            .forEach { println(it) }
        return "-"
    }

    override fun part2(input: List<String>): String {
        return "-"
    }

    private fun resolveMutlilevelCode(code: String, keyPads: List<Matrix<Char>>, depth: Int): List<String> {
        if (depth >= keyPads.size - 1) return resolveCode(code, keyPads[depth])

        val cacheKey = Triple(code, keyPads, depth)
        if (cacheKey in resolveMultilevelCodeCache) return resolveMultilevelCodeCache[cacheKey]!!

        val result = resolveMutlilevelCode(code, keyPads, depth + 1).flatMap {
            resolveCode(it, keyPads[depth])
        }.groupBy { it.length }.minByOrNull { it.key }?.value ?: emptyList()

        resolveMultilevelCodeCache[cacheKey] = result
        return result
    }

    private fun resolveCode(code: String, keyPad: Matrix<Char>): List<String> {
        if (code.length == 1) return findAllShortestSequencesToMoveAndPress('A', code[0], keyPad).map { it + "A" }
        val cacheKey = Pair(code, keyPad)
        if (cacheKey in resolveCodeCache) return resolveCodeCache[cacheKey]!!

        val untilHere = resolveCode(code.substring(0, code.length - 1), keyPad)
        val result = untilHere.flatMap { u ->
            findAllShortestSequencesToMoveAndPress(
                code[code.length - 2],
                code[code.length - 1],
                keyPad
            ).map { u + it + "A" }
        }.groupBy { it.length }.minBy { it.key }.value
        resolveCodeCache[cacheKey] = result
        return result
    }

    private fun precalcAllShortestPaths(keyPad: Matrix<Char>): Map<Pair<Char, Char>, List<String>> {
        val cells = keyPad.cells().filter { it.value != '.' }
        val map = mutableMapOf<Pair<Char, Char>, List<String>>()
        for (i in 0 until cells.size) {
            for (j in 0 until cells.size) {
                map[Pair(cells[i].value, cells[j].value)] =
                    findAllShortestSequencesToMoveAndPress(cells[i].value, cells[j].value, keyPad)
            }
        }
        return map
    }

    private fun findAllShortestSequencesToMoveAndPress(from: Char, to: Char, keyPad: Matrix<Char>): List<String> {
        val source = keyPad.allCellsWithValue(from).single()
        val target = keyPad.allCellsWithValue(to).single()
        val deadKey = keyPad.allCellsWithValue('.').single()

        val vertical = if (target.i > source.i) "v" else "^"
        val horizontal = if (target.j > source.j) ">" else "<"

        val iDiff = (target.i - source.i).absoluteValue
        val jDiff = (target.j - source.j).absoluteValue

        val iDead = (deadKey.i - source.i).absoluteValue
        val jDead = (deadKey.j - source.j).absoluteValue

        fun findCombos(i: Int, j: Int, iDead: Int = -1, jDead: Int = -1): List<String> {
            if (i == 0 && j == 0) return listOf("")
            if (iDead == 0 && jDead == 0) return emptyList()
            val results = mutableListOf<String>()
            if (i != 0) {
                findCombos(i - 1, j, iDead - 1, jDead).map { "$vertical$it" }.forEach { results.add(it) }
            }
            if (j != 0) {
                findCombos(i, j - 1, iDead, jDead - 1).map { "$horizontal$it" }.forEach { results.add(it) }
            }
            return results
        }

        val result = if (source.i == deadKey.i && target.j == deadKey.j && iDiff > 0) {
            findCombos(iDiff, jDiff, iDead, jDead)
        } else if (source.j == deadKey.j && target.i == deadKey.i) {
            findCombos(iDiff, jDiff, iDead, jDead)
        } else {
            findCombos(iDiff, jDiff)
        }

        return result
    }
}