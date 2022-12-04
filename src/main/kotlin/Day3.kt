import java.io.File

class Day3 : Day {
    private fun readInput() = File(INPUT_PATH).readLines()

    private fun getPriority(x: Char) = when {
        x.isLowerCase() -> x - 'a' + 1
        x.isUpperCase() -> x - 'A' + 27
        else -> error("Invalid symbol")
    }

    private fun getPriorityOfRucksack(s: String): Int {
        val (first, second) = s.toList().twoParts().map { it.toSet() }
        return first.intersect(second).sumOf { getPriority(it) }
    }

    override fun part1(): Int = readInput().sumOf { getPriorityOfRucksack(it) }

    override fun part2(): Int = readInput()
        .map { it.toSet() }
        .chunked(3)
        .sumOf {
            val initial = ('a'..'z').toSet() + ('A'..'Z').toSet()
            it.fold(initial) { acc, chars -> acc.intersect(chars) }.sumOf { char -> getPriority(char) }
        }

    private fun <T> List<T>.twoParts() = chunked(size / 2)

    companion object {
        const val INPUT_PATH = "day3.in"
//        const val INPUT_PATH = "day3.in.exmp"

    }
}