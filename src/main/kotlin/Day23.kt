fun main() = Day23().run()

class Day23 : AbstractDailyPuzzle() {

    override fun part1(input: List<String>): String {
        val computers = readInput(input)

        val result = computers.values.flatMap { computer ->
            computer.peers.flatMap { peerName ->
                val peer = computers[peerName]!!
                peer.peers
                    .filter { it in computer.peers }
                    .map { setOf(computer.name, peer.name, it) }
            }.filter { it.any { it.startsWith("t") } }.toSet()
        }.toSet()

        return result.count().toString()
    }

    override fun part2(input: List<String>): String {
        val computers = readInput(input)
        val networks = mutableSetOf<Set<String>>()
        computers.values.forEach { computer ->
            val newNetworks = networks.filter { network ->
                computer.peers.containsAll(network)
            }.map { it + computer.name }
            networks.addAll(newNetworks)
            networks.add(setOf(computer.name))
        }
        return networks.groupBy { it.size }.maxBy { it.key }.value.single().sorted().joinToString(",")
    }


    private fun readInput(input: List<String>): Map<String, Computer> {
        val map = mutableMapOf<String, Computer>()
        input.forEach {
            val (name1, name2) = it.split("-")
            val computer1 = map[name1] ?: Computer(name1)
            val computer2 = map[name2] ?: Computer(name2)

            computer1.peers.add(computer2.name)
            computer2.peers.add(computer1.name)

            map[name1] = computer1
            map[name2] = computer2
        }
        return map
    }

    internal data class Computer(
        val name: String,
        val peers: MutableSet<String> = mutableSetOf()
    )
}

