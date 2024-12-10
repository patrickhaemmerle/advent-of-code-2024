fun main() = Day09().run()

class Day09 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val decompressedDisk = readInput(input)

        val compressedDisk = mutableListOf<DiskSlot>()
        while (decompressedDisk.isNotEmpty()) {
            val current = decompressedDisk.removeFirst()
            if (current is File) {
                compressedDisk.add(current)
            } else {
                while (decompressedDisk.isNotEmpty() && decompressedDisk.last() is EmptySpace) decompressedDisk.removeLast()
                if (decompressedDisk.isNotEmpty()) {
                    val last = decompressedDisk.removeLast() as File
                    val freeSpace = current.length
                    if (last.length <= freeSpace) {
                        compressedDisk.add(last)
                        if (last.length < freeSpace) {
                            decompressedDisk.addFirst(EmptySpace(freeSpace - last.length))
                        }
                    } else {
                        compressedDisk.add(File(freeSpace, last.id))
                        decompressedDisk.addLast(File(last.length - freeSpace, last.id))
                    }
                }
            }
        }

        var pos = 0
        var sum = 0L
        compressedDisk.forEach {
            if (it is File) {
                for (i in 0 until it.length) {
                    sum += (pos * it.id)
                    pos++
                }
            }
        }

        return sum.toString()
    }

    override fun part2(input: List<String>): String {
        val disk = readInput(input)

        var attemptToMoveIndex = disk.size - 1
        while (attemptToMoveIndex > 0) {
            if (disk[attemptToMoveIndex] is File) {
                for (i in 1 until attemptToMoveIndex) {
                    if (disk[i] is EmptySpace && disk[i].length >= disk[attemptToMoveIndex].length) {
                        val emptySpace = EmptySpace(disk[i].length - disk[attemptToMoveIndex].length)
                        val move = disk[attemptToMoveIndex]
                        disk[attemptToMoveIndex] = EmptySpace(move.length)
                        disk[i] = emptySpace
                        disk.add(i, move)
                        break
                    }
                }
            }
            attemptToMoveIndex--
        }

        var pos = 0
        var sum = 0L
        disk
            .forEach {
                if (it is File) {
                    for (i in 0 until it.length) {
                        sum += (pos * it.id)
                        pos++
                    }
                } else {
                    pos += it.length
                }
            }

        return sum.toString()
    }

    private fun readInput(input: List<String>): ArrayDeque<DiskSlot> {
        val diskMap = input.first().toCharArray()
        val decompressedDisk = ArrayDeque<DiskSlot>()

        diskMap.forEachIndexed() { i, value ->
            if (i % 2 == 0) {
                decompressedDisk.addLast(File(value.digitToInt(), i / 2))
            } else {
                decompressedDisk.addLast(EmptySpace(value.digitToInt()))
            }
        }
        return decompressedDisk
    }

}

interface DiskSlot {
    val length: Int
}

data class EmptySpace(
    override val length: Int
) : DiskSlot {
    override fun toString(): String = ".".repeat(length)
}

data class File(
    override val length: Int,
    val id: Int
) : DiskSlot{
    override fun toString(): String = id.toString().repeat(length)
}