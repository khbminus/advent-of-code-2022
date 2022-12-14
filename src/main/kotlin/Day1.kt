import java.io.File

class Day1 : Day {
    override fun part1() = getInput().max()
    override fun part2() = getInput().sorted().takeLast(3).sum()


    private fun getInput() = File(INPUT_FILE)
        .readLines()
        .splitBy("")
        .map { elfsSnacks -> elfsSnacks.sumOf { it.toInt() } }

    companion object {
        const val INPUT_FILE = "day1.in"
    }
}
