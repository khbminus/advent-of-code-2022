import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day12 : Day {

    private fun findPath(start: List<Pair<Int, Int>>, end: List<Pair<Int, Int>>): Int {
        val grid = readInput()
        val deque = LinkedList<Pair<Int, Int>>()
        val used = List(grid.size) { MutableList(grid[it].length) { false } }
        val dist = List(grid.size) { MutableList(grid[it].length) { Int.MAX_VALUE } }
        start.forEach {(x, y) ->
            dist[x][y] = 0
            deque.push(x to y)
        }
        while (deque.isNotEmpty()) {
            val (x, y) = deque.pop()
            if (used[x][y]) {
                continue
            }
//            println(grid[x][y])
            used[x][y] = true
            SHIFT.map { (sx, sy) -> x + sx to y + sy }.filter { (nx, ny) ->
                nx in grid.indices && ny in grid[nx].indices
                        && grid[nx][ny].getElevation() - grid[x][y].getElevation() <= 1
            }.forEach { (nx, ny) ->
                if (dist[nx][ny] > dist[x][y] + 1) {
                    dist[nx][ny] = dist[x][y] + 1
                    deque.add(nx to ny)
                }
            }
        }
        return end.minOf { (x, y) -> dist[x][y] }
    }
    override fun part1() = findPath(startPosition(readInput()), endPosition(readInput()))

    override fun part2() = findPath(find(readInput(), 'a'), endPosition(readInput()))


    private fun startPosition(grid: List<String>) = find(grid, 'S')
    private fun endPosition(grid: List<String>) = find(grid, 'E')

    private fun Char.getElevation() = when (this) {
        'S' -> 'a'
        'E' -> 'z'
        else -> this
    } - 'a'

    private fun find(grid: List<String>, c: Char) = buildList {
            for (i in grid.indices) {
                for (j in grid[i].indices) {
                    if (grid[i][j] == c) {
                        add(i to j)
                    }
                }
            }
        }

    private fun readInput() = File(INPUT_PATH).readLines()

    companion object {
        private const val INPUT_PATH = "day12.in"
//        private const val INPUT_PATH = "day12.in.exmp"
        private val SHIFT = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    }
}