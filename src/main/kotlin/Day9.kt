import java.io.File
import kotlin.math.abs
import kotlin.math.sign

class Day9 : Day {

    private val MOVES = mapOf("U" to Pair(1, 0), "D" to Pair(-1, 0), "L" to Pair(0, -1), "R" to Pair(0, 1))
    override fun part1(): Any {
        val commands = readInput()
        var head = 0 to 0
        var tail = 0 to 0
        val visited = mutableSetOf<Pair<Int, Int>>()
        commands.forEach { (dir, cnt) ->
            repeat(cnt) {
                head += MOVES[dir]!!
                visited.add(tail)
                while (dist(head, tail) > 1) {
                    tail += (head.first - tail.first).sign to (head.second - tail.second).sign
                    visited.add(tail)
                }
            }
        }
        return visited.size
    }

    override fun part2(): Any {
        val commands = readInput()
        val rope = MutableList(10) { 0 to 0 }
        val visited = mutableSetOf<Pair<Int, Int>>()
        commands.forEach { (dir, cnt) ->
            repeat(cnt) {
                rope[0] += MOVES[dir]!!
                visited.add(rope.last())
                for (i in 1 until rope.size) {
                    while (dist(rope[i - 1], rope[i]) > 1) {
                        rope[i] += (rope[i - 1].first - rope[i].first).sign to (rope[i - 1].second - rope[i].second).sign
                        visited.add(rope.last())
                    }
                }
            }
        }
        return visited.size
    }

    private fun readInput() = File(INPUT_PATH).readLines().map {
        val splitted = it.split(" ")
        splitted[0] to splitted[1].toInt()
    }

    private fun dist(a: Pair<Int, Int>, b: Pair<Int, Int>) =
        abs(a.first - b.first).coerceAtLeast(abs(a.second - b.second))

    private operator fun Pair<Int, Int>.plus(b: Pair<Int, Int>) = (first + b.first) to (second + b.second)

    companion object {
        private const val INPUT_PATH = "day9.in"
//        private const val INPUT_PATH = "day9.in.exmp"
    }
}