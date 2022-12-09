import java.io.File

class Day8 : Day {

    private val grid = readInput()
    override fun part1(): Int {
        val visible = MutableList(grid.size) { i -> MutableList(grid[i].size) { false } }
        grid.forEachIndexed { row, line ->
            line.forEachIndexed { col, elem ->
                visible[row][col] = (line.subList(0, col).maxOrNull() ?: -1) < elem
                        || (line.subList(col + 1, line.size).maxOrNull() ?: -1) < elem
            }
        }
        grid[0].indices.forEach { col ->
            val line = List(grid.size) { row -> grid[row][col] }
            line.forEachIndexed { row, elem ->
                visible[row][col] = visible[row][col]
                        || (line.subList(0, row).maxOrNull() ?: -1) < elem
                        || (line.subList(row + 1, line.size).maxOrNull() ?: -1) < elem
            }
        }
        return visible.sumOf { it.count { it } }
    }

    override fun part2(): Int {
        val score  = MutableList(grid.size) { i -> MutableList(grid[i].size) { 1 } }
        grid.forEachIndexed { row, line ->
            line.forEachIndexed { col, elem ->
                val lft = col - line.subList(0, col).indexOfLast { elem <= it  }.coerceAtLeast(0)
                var rgt = line.subList(col + 1, line.size).indexOfFirst { elem <= it }
                if (rgt == -1) {
                    rgt = line.size - col - 1
                } else {
                    rgt++
                }
                score[row][col] = lft * rgt
            }
        }
        grid[0].indices.forEach { col ->
            val line = List(grid.size) { row -> grid[row][col] }
            line.forEachIndexed { row, elem ->
                val lft = row - line.subList(0, row).indexOfLast { elem <= it  }.coerceAtLeast(0)
                var rgt = line.subList(row + 1, line.size).indexOfFirst { elem <= it }
                if (rgt == -1) {
                    rgt = line.size - row - 1
                } else {
                    rgt++
                }
                score[row][col] = score[row][col] * lft * rgt
            }
        }
        return score.maxOf { it.max() }
    }

    private fun readInput() = File(INPUT_PATH).readLines().map { it.toList().map { it.digitToInt() } }


    companion object {
        private const val INPUT_PATH = "day8.in"
//        private const val INPUT_PATH = "day8.in.exmp"
    }
}