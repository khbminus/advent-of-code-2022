import java.io.File
import kotlin.math.sign

class Day14 : Day {

    lateinit var field: MutableSet<Pair<Int, Int>>
    lateinit var xRange: IntRange
    lateinit var yRange: IntRange
    private fun initField() {
        val paths = readInput()
        field = buildSet {
            paths.forEach {
                it.zipWithNext().forEach {
                    val diff = it.second - it.first
                    var now = it.first
                    while (now != it.second) {
                        add(now)
                        now += diff.first.sign to diff.second.sign
                    }
                    add(it.second)
                }
            }
        }.toMutableSet()
        xRange = field.minOf { it.first }..field.maxOf { it.first }
        yRange = field.minOf { it.second }..field.maxOf { it.second }
    }

    private enum class FallResult {
        INFINITY, BLOCKED, FREE
    }

    private fun canFallDown(pos: Pair<Int, Int>, isHasFloor: Boolean): FallResult {
        val (x, y) = pos
        return when {
            !isHasFloor && (x !in xRange || y > yRange.last) -> FallResult.INFINITY
            field.contains(pos) || (isHasFloor && y == yRange.last + 2) -> FallResult.BLOCKED
            else -> FallResult.FREE
        }
    }

    private fun simulateSand(isHasFloor: Boolean): Boolean {
        var x = 500
        var y = 0
        if (canFallDown(x to y, isHasFloor) == FallResult.BLOCKED) {
            return false
        }
        while (true) {
//            println("$x $y")
            when {
                canFallDown(x to (y + 1), isHasFloor) == FallResult.INFINITY -> return false
                canFallDown(x to (y + 1), isHasFloor) == FallResult.FREE -> {}

                canFallDown((x - 1) to (y + 1), isHasFloor) == FallResult.INFINITY -> return false
                canFallDown((x - 1) to (y + 1), isHasFloor) == FallResult.FREE -> {
                    x--
                }

                canFallDown((x + 1) to (y + 1), isHasFloor) == FallResult.INFINITY -> return false
                canFallDown((x + 1) to (y + 1), isHasFloor) == FallResult.FREE -> {
                    x++
                }

                else -> {
                    field.add(x to y)
                    return true
                }
            }
            y++
        }
    }

    override fun part1(): Int {
        initField()
        var cnt = 0
        while (simulateSand(false)) {
            cnt++
        }
        return cnt
    }

    override fun part2(): Any {
        initField()
        var cnt = 0
        while (simulateSand(true)) {
            cnt++
        }
        return cnt
    }


    private fun readInput() = File(INPUT_PATH)
        .readLines()
        .map {
            it.split("->")
                .map {
                    val splitted = it.trim().split(",")
                    splitted[0].toInt() to splitted[1].toInt()
                }
        }

    companion object {
        const val INPUT_PATH = "day14.in"
//        const val INPUT_PATH = "day14.in.exmp"
    }
}