class Day11 : Day {
    override fun part1(): Any {
        val monks = List(MONKIES.size) { MONKIES[it].copy() }
        val counters = MutableList(MONKIES.size) { 0 }
        repeat(20) {
            monks.forEachIndexed { idx, monkey ->
                monkey.items.forEach {
                    counters[idx]++
                    val newElem = monkey.modification(it) / 3
                    monks[monkey.getNext(newElem)].items.add(newElem)
                }
                monkey.items.clear()
            }
        }
        return counters.sorted().takeLast(2).fold(1, Int::times)
    }

    override fun part2(): Any {
        val monks = listOf(
            Monkey(mutableListOf(50, 70, 54, 83, 52, 78), { x -> x * 3 }, buildTest(11, 2, 7)),
            Monkey(mutableListOf(71, 52, 58, 60, 71), { x -> x * x }, buildTest(7, 0, 2)),
            Monkey(mutableListOf(66, 56, 56, 94, 60, 86, 73), { x -> x + 1 }, buildTest(3, 7, 5)),
            Monkey(mutableListOf(83, 99), { x -> x + 8 }, buildTest(5, 6, 4)),
            Monkey(mutableListOf(98, 98, 79), { x -> x + 3 }, buildTest(17, 1, 0)),
            Monkey(mutableListOf(76), { x -> x + 4 }, buildTest(13, 6, 3)),
            Monkey(mutableListOf(52, 51, 84, 54), { x -> x * 17 }, buildTest(19, 4, 1)),
            Monkey(mutableListOf(82, 86, 91, 79, 94, 92, 59, 94), { x -> x + 7 }, buildTest(2, 5, 3)),
        )
        val counters = MutableList(MONKIES.size) { 0L }
        println(MODULO)
        repeat(10000) {
            monks.forEachIndexed { idx, monkey ->
                monkey.items.forEach {
                    counters[idx]++
                    val newElem = monkey.modification(it) // % MODULO
                    monks[monkey.getNext(newElem)].items.add(newElem % MODULO)
                }
                monkey.items.clear()
            }
        }
        return counters.sorted().takeLast(2).fold(1, Long::times)
    }

    private data class Monkey(val items: MutableList<Long>, val modification: (Long) -> Long, val getNext: (Long) -> Int)

    companion object {
        private fun buildTest(modulo: Long, trueVal: Int, falseVal: Int) =
            { x: Long -> if (x % modulo == 0L) trueVal else falseVal }

        private val MONKIES = listOf(
            Monkey(mutableListOf(50, 70, 54, 83, 52, 78), { x -> x * 3 }, buildTest(11, 2, 7)),
            Monkey(mutableListOf(71, 52, 58, 60, 71), { x -> x * x }, buildTest(7, 0, 2)),
            Monkey(mutableListOf(66, 56, 56, 94, 60, 86, 73), { x -> x + 1 }, buildTest(3, 7, 5)),
            Monkey(mutableListOf(83, 99), { x -> x + 8 }, buildTest(5, 6, 4)),
            Monkey(mutableListOf(98, 98, 79), { x -> x + 3 }, buildTest(17, 1, 0)),
            Monkey(mutableListOf(76), { x -> x + 4 }, buildTest(13, 6, 3)),
            Monkey(mutableListOf(52, 51, 84, 54), { x -> x * 17 }, buildTest(19, 4, 1)),
            Monkey(mutableListOf(82, 86, 91, 79, 94, 92, 59, 94), { x -> x + 7 }, buildTest(2, 5, 3)),
        )
        private val MODULO = listOf(11L, 7L, 3L, 5L, 17L, 13L, 19L, 2L).fold(1, Long::times)
    }
}