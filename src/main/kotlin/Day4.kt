fun main() = Day4().run()

class Day4 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val puzzle = readInput(input)
        val width = puzzle.first().size
        val height = puzzle.size

        var counts = 0

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (searchXmas(i, j, 0, 1, puzzle)) counts++
                if (searchXmas(i, j, 0, -1, puzzle)) counts++
                if (searchXmas(i, j, 1, 0, puzzle)) counts++
                if (searchXmas(i, j, -1, 0, puzzle)) counts++
                if (searchXmas(i, j, 1, 1, puzzle)) counts++
                if (searchXmas(i, j, 1, -1, puzzle)) counts++
                if (searchXmas(i, j, -1, 1, puzzle)) counts++
                if (searchXmas(i, j, -1, -1, puzzle)) counts++
            }
        }

        return counts.toString()
    }

    private fun searchXmas(
        i: Int,
        j: Int,
        iStep: Int,
        jStep: Int,
        puzzle: Array<CharArray>
    ): Boolean {
        val width = puzzle.first().size
        val height = puzzle.size
        if (i + 3 * iStep >= height || j + 3 * jStep >= width) return false
        if (i + 3 * iStep < 0 || j + 3 * jStep < 0) return false
        val found = puzzle[i][j] == 'X'
                && puzzle[(i + height + iStep) % height][(j + width + jStep) % width] == 'M'
                && puzzle[(i + height + 2 * iStep) % height][(j + width + 2 * jStep) % width] == 'A'
                && puzzle[(i + height + 3 * iStep) % height][(j + width + 3 * jStep) % width] == 'S'
        return found
    }

    override fun part2(input: List<String>): String {
        val puzzle = readInput(input)
        val width = puzzle.first().size
        val height = puzzle.size

        var counts = 0

        for (i in 0 until height) {
            for (j in 0 until width) {
                if (searchCrossedMas(i, j, puzzle)) counts++
            }
        }

        return counts.toString()
    }

    private fun searchCrossedMas(
        i: Int,
        j: Int,
        puzzle: Array<CharArray>
    ): Boolean {
        val width = puzzle.first().size
        val height = puzzle.size
        if (j < 1 || i < 1 || j >= width - 1 || i >= height - 1) return false
        val found = puzzle[i][j] == 'A'
                && (puzzle[i - 1][j - 1] == 'M' && puzzle[i + 1][j + 1] == 'S' || puzzle[i - 1][j - 1] == 'S' && puzzle[i + 1][j + 1] == 'M')
                && (puzzle[i + 1][j - 1] == 'M' && puzzle[i - 1][j + 1] == 'S' || puzzle[i + 1][j - 1] == 'S' && puzzle[i - 1][j + 1] == 'M')
        return found
    }

    private fun readInput(input: List<String>) =
        input.map { it.toCharArray() }.toTypedArray()
}