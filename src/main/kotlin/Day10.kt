import java.io.File

class Day10 : Day {

    private fun getValues(): List<Int> {
        val commands = readInput()
        val values = mutableListOf(1)
        commands.forEach {
            values.add(values.last())
            if (it is COMMAND.ADDX) {
                values.add(values.last() + it.v)
            }
        }
        return values
    }
    override fun part1(): Int {
        return getValues()
            .withIndex()
            .filter { it.index % 40 == 19 && it.index <= 219 }
            .sumOf { (it.index + 1) * it.value }
    }

    override fun part2(): String {
        val values = getValues()
        return "\n" + values.dropLast(1).mapIndexed { index, i ->
            when (index % 40) {
                in i-1..i+1 -> '#'
                else -> '.'
            }
        }.chunked(40).map { it.joinToString("") }.joinToString("\n")
    }

    private fun readInput() = File(INPUT_PATH).readLines().map {
        val args = it.split(" ")
        when (args[0]) {
            "noop" -> COMMAND.NOOP()
            "addx" -> COMMAND.ADDX(args[1].toInt())
            else -> error("unknown command")
        }
    }

    private sealed class COMMAND {
        class NOOP : COMMAND()
        class ADDX(val v: Int) : COMMAND()
    }

    companion object {
                const val INPUT_PATH = "day10.in"
//        const val INPUT_PATH = "day10.in.exmp"
    }
}