import java.io.File

class Day17 : Day {
    private var field: MutableSet<Pair<Long, Long>> = mutableSetOf()
    private var maxHeightNow = 0L
    private val volcano = File(INPUT_PATH).readLines()[0]

    private fun canFallDown(pos: Pair<Long, Long>): FallResult {
        val (x, y) = pos
        return when {
            x !in 0..6 || y < 0 || field.contains(pos) -> FallResult.BLOCKED
            else -> FallResult.FREE
        }
    }

    private fun canShapeFall(shape: List<Pair<Long, Long>>): Boolean = shape.all { canFallDown(it) == FallResult.FREE }

    private fun getShift(x: Char) = when (x) {
        '<' -> -1L to 0L
        '>' -> 1L to 0L
        else -> error("badSymbol")
    }

    private var currentIdx = 0

    private fun dropShape(shape: List<Pair<Long, Long>>) {
        val yOffset = maxHeightNow + 3 - shape.minOf { it.second }
        val xOffset = 2 - shape.minOf { it.first }
        var movingShape = shape.map { it + (xOffset to yOffset) }
        while (true) {
            val newShapeStage1 = movingShape.map { it + getShift(volcano[currentIdx]) }
            if (canShapeFall(newShapeStage1)) {
                movingShape = newShapeStage1
            }
            currentIdx = (currentIdx + 1) % volcano.length
            val newShapeStage2 = movingShape.map { it + (0L to -1L) }
            if (canShapeFall(newShapeStage2)) {
                movingShape = newShapeStage2
            } else {
                field.addAll(movingShape)
                maxHeightNow = maxHeightNow.coerceAtLeast(movingShape.maxOf { it.second + 1 })
                break
            }
        }
    }

    private fun simulate(x: Long) {
        for (i in 0 until x) {
            val was = maxHeightNow
            dropShape(FORMS[(i % FORMS.size).toInt()])
            println(maxHeightNow - was)
        }
    }

    private fun findCycle(x: List<Long>): Int {
        for (len in 1..(x.size / 2)) {
            var success = true
            for (i in x.lastIndex downTo 2 * len) {
                if (x[i] != x[i - len]) {
                    success = false
                    break
                }
            }
            if (success) {
                return len
            }
        }
        return -1
    }

    override fun part1(): Any {
        simulate(PART1_CONST)
        return maxHeightNow
    }

    override fun part2(): Any {
        val diffs = mutableListOf<Long>()
        repeat(1_000_000) {
            val was = maxHeightNow
            dropShape(FORMS[(it % FORMS.size)])
            diffs.add(maxHeightNow - was)
        }
        val cycle = findCycle(diffs)
        require(cycle > 0) { "$cycle <= 0" }

        field.clear()
        maxHeightNow = 0
        currentIdx = 0
        val cycleHeight = diffs.takeLast(cycle).sum()
        val remainder = PART2_CONST % cycle
        simulate(remainder + 3 * cycle)
        return maxHeightNow + cycleHeight * (PART2_CONST / cycle - 3)
    }

    private enum class FallResult {
        INFINITY, BLOCKED, FREE
    }

    companion object {
        private const val INPUT_PATH = "day17.in"
        private val FORMS = listOf(
            listOf(0 to 0, 1 to 0, 2 to 0, 3 to 0),
            listOf(0 to 0, 1 to 0, -1 to 0, 0 to 1, 0 to -1),
            listOf(0 to 0, -1 to 0, -2 to 0, 0 to 1, 0 to 2),
            listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3),
            listOf(0 to 0, 0 to 1, -1 to 0, -1 to 1)
        ).map { it.map { (a, b) -> a.toLong() to b.toLong() } }
        private const val PART1_CONST = 2022L
        private const val PART2_CONST = 1000000000000
    }
}

fun main() {
    println(Day17().part1())
    println(Day17().part2())
}