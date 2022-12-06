import java.io.File

class Day5 : Day {
    override fun part1(): String {
        val (stack, commands) = getInput()
        return buildAnswer(move(stack, commands, true))
    }

    override fun part2(): String {
        val (stack, commands) = getInput()
        return buildAnswer(move(stack, commands, false))
    }

    private fun buildAnswer(stack: Map<String, MutableList<Char>>) = buildString {
        stack.keys.sorted().forEach {
            append(stack[it]!!.last())
        }
    }

    private fun move(
        stack: Map<String, MutableList<Char>>,
        commands: List<MoveCommand>,
        isReverse: Boolean
    ): Map<String, MutableList<Char>> = stack.also {
        commands.forEach { (amount, from, to) ->
            stack[to]?.addAll(stack[from]?.takeLast(amount)?.let { if (isReverse) it.reversed() else it }
                ?: emptyList())
            repeat(amount) { stack[from]?.removeLast() }
        }
    }

    private fun getInput(): Pair<MutableMap<String, MutableList<Char>>, List<MoveCommand>> {
        val lines = File(INPUT_PATH).readLines()
        val (startStack, commands) = lines.splitBy("")
        val stacks = startStack
            .map {
                it.chunked(4)
                    .map { it.trim() }
            }
        val stack: MutableMap<String, MutableList<Char>> = mutableMapOf()

        stacks.dropLast(1).reversed().forEach { line ->
            line.forEachIndexed { index, s ->
                if (s.isBlank()) {
                    return@forEachIndexed
                }
                stack.compute(stacks.last()[index]) { k, v ->
                    val v1 = v ?: mutableListOf()
                    v1.add(s[1])
                    v1
                }
            }
        }

        val matched = commands.map {
            val match = COMMAND_REGEX.find(it)!!
            val (amount, from, to) = match.destructured
            MoveCommand(amount.toInt(), from, to)
        }
        return stack to matched

    }

    private data class MoveCommand(val amount: Int, val from: String, val to: String)

    companion object {
                const val INPUT_PATH = "day5.in"
//        const val INPUT_PATH = "day5.in.exmp"
        val COMMAND_REGEX = Regex("move (\\d+) from (\\d+) to (\\d+)")
    }
}