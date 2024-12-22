fun main() = Day22().run()

class Day22 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        return input.sumOf { calculateNthSecret(it.toLong(), 2000) }.toString()
    }

    override fun part2(input: List<String>): String {
        val changesToTotalGainMap = mutableMapOf<List<Int>, Int>()
        input
            .map { calculateNPriceChanges(it.toLong(), 2000) }
            .forEach { monkey ->
                val seen = mutableSetOf<List<Int>>()
                for (current in 5..monkey.size) {
                    val relevantPrices = monkey.subList(current - 4, current)
                    val key = relevantPrices.map { it.change!! }
                    if (key in seen) continue
                    seen.add(key)
                    changesToTotalGainMap.compute(key) { _, oldValue -> (oldValue ?: 0) + relevantPrices.last().price }
                }
            }

        return changesToTotalGainMap.maxBy { it.value }.value.toString()
    }

    private fun calculateNthSecret(initial: Long, n: Int): Long {
        var result = initial
        for (i in 0 until n) result = calculateNextSecret(result)
        return result
    }

    private fun calculateNPriceChanges(initialSecret: Long, n: Int): MutableList<Price> {
        val prices = mutableListOf(Price((initialSecret % 10).toInt(), null, initialSecret))
        for (i in 0 until n) {
            val lastPrice = prices.last()
            val newSecret = calculateNextSecret(lastPrice.secret)
            val newPrice = (newSecret % 10).toInt()
            val change = newPrice - lastPrice.price
            prices.add(Price(newPrice, change, newSecret))
        }
        return prices
    }

    private fun calculateNextSecret(secret: Long): Long {
        val prune = 16777216
        var newSecret = ((secret * 64) xor secret) % prune
        newSecret = ((newSecret / 32) xor newSecret) % prune
        newSecret = ((newSecret * 2048) xor newSecret) % prune
        return newSecret
    }

    data class Price(
        val price: Int,
        val change: Int?,
        val secret: Long,
    )
}