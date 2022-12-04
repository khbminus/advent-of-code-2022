import java.io.File

class Day4 : Day {
    override fun part1() = readInput().count { it.first.isSubsetOf(it.second) || it.second.isSubsetOf(it.first) }

    override fun part2() = readInput().count { it.first.isIntersects(it.second) }

    private fun readInput() = File(INPUT_PATH)
        .readLines()
        .map {
            val twoRanges = it.split(",")
                .map {
                    val list = it.split("-").map { it.toInt() }
                    require(list.size == 2)
                    list[0]..list[1]
                }
            require(twoRanges.size == 2)
            twoRanges[0] to twoRanges[1]
        }

    private fun IntRange.isSubsetOf(rhs: IntRange) = first >= rhs.first && last <= rhs.last
    private fun IntRange.isIntersects(rhs: IntRange) = rhs.last >= first && last >= rhs.first

    companion object {
        const val INPUT_PATH = "day4.in"
//        const val INPUT_PATH = "day4.in.exmp"
    }
}