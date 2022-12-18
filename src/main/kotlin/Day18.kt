import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day18 : Day {
    override fun part1(): Any {
        val cubes = readInput()
        return calculateAnswer(cubes)
    }

    private fun calculateAnswer(cubes: List<List<Int>>) = 6 * cubes.size - cubes.sumOf { a ->
        cubes.count { b ->
            isConnected(a, b)
        }
    }

    private fun isConnected(cube1: List<Int>, cube2: List<Int>) =
        cube1.zip(cube2).count { (a, b) -> a != b } == 1 &&
                cube1.zip(cube2).sumOf { (a, b) -> (a - b).absoluteValue } == 1

    private fun bfs(
        cubes: Set<List<Int>>,
        start: List<Int>,
        external: Set<List<Int>>,
        updatedOn: MutableMap<List<Int>, Int>,
        iteration: Int,
        bounds: List<IntRange>
    ): Boolean {
        val queue: Queue<List<Int>> = LinkedList()
        queue.add(start)
        var isExternal = false
        updatedOn[start] = iteration
        while (queue.isNotEmpty()) {
            val front = queue.remove()
            if (front in external || front.any { it == 0 }) {
                isExternal = true
            }
            for (shift in SHIFTS) {
                val next = front.mapIndexed { index, i -> shift[index] + i }
                if (next.zip(bounds).any { (a, b) -> a !in b }) {
                    continue
                }
                if (next !in updatedOn && next !in cubes) {
                    updatedOn[next] = iteration
                    queue.add(next)
                }
            }
        }
        return isExternal
    }

    override fun part2(): Any {
        val cubes = readInput().toMutableSet()
        val external = mutableSetOf<List<Int>>()
        val internal = mutableSetOf<List<Int>>()
        val updatedOn = mutableMapOf<List<Int>, Int>()
        val xBounds = 0..cubes.maxOf { it[0] + 1 }
        val yBounds = 0..cubes.maxOf { it[1] + 1 }
        val zBounds = 0..cubes.maxOf { it[2] + 1 }
        val bounds = listOf(xBounds, yBounds, zBounds)
        var iteration = 0
        for (x in xBounds) {
            for (y in yBounds) {
                for (z in zBounds) {
                    if (bfs(cubes, listOf(x, y, z), external, updatedOn, iteration, bounds)) {
                        external.addAll(updatedOn.filter { it.value == iteration }.keys)
                    } else {
                        internal.addAll(updatedOn.filter { it.value == iteration }.keys)
                    }
                    iteration++
                }
            }
        }
        cubes.addAll(internal)
        return calculateAnswer(cubes.toList())
    }

    private fun readInput() = File(INPUT_PATH).readLines().map {
        it.split(",").map { it.toInt() }
    }

    companion object {
        private const val INPUT_PATH = "day18.in"
//        private const val INPUT_PATH = "day18.in.exmp"
        val SHIFTS = (0..2).flatMap { idx ->
            (-1..1 step 2).map {
                val list = mutableListOf(0, 0, 0)
                list[idx] = it
                list
            }
        }
    }
}

fun main() {
    println(Day18().part1())
    println(Day18().part2())
//    println(Day18.SHIFTS)
}