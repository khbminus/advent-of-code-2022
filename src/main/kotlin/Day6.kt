import java.io.File

class Day6 : Day {
    override fun part1() = readInput().toList().windowed(size = 4, step = 1).indexOfFirst { it.toSet().size == 4 } + 4

    override fun part2() = readInput().toList().windowed(size = 14, step = 1).indexOfFirst { it.toSet().size == 14 } + 14

    private fun readInput() = File(INPUT_PATH).readLines()[0]

    companion object {
                private const val INPUT_PATH = "day6.in"
//        private const val INPUT_PATH = "day6.in.exmp"
    }
}