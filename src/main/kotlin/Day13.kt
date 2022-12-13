import java.io.File

class ListParser {
    sealed class NestedList : Comparable<NestedList> {
        class JustList(val value: List<NestedList>) : NestedList() {
            override fun compareTo(other: NestedList): Int {
                if (other is Const) {
                    return compareTo(JustList(listOf(other)))
                }
                require(other is JustList)
                return value.zip(other.value).map { (a, b) -> a.compareTo(b) }.firstOrNull { it != 0 }
                    ?: value.size.compareTo(other.value.size)
            }

            override fun toString(): String {
                return value.toString()
            }
        }

        class Const(val value: Int) : NestedList() {
            override fun toString(): String {
                return value.toString()
            }

            override fun compareTo(other: NestedList): Int = when (other) {
                is Const -> value.compareTo(other.value)
                is JustList -> JustList(listOf(this)).compareTo(other)
            }
        }
    }

    private class StringHolder(private val s: String) {
        private var index = 0
        fun isEnd() = s.length == index
        fun getSymbol() = s[index]
        fun getNext() {
            index++
        }
    }

    private fun parseNumber(expr: StringHolder) = NestedList.Const(
        buildString {
            while (!expr.isEnd() && expr.getSymbol().isDigit()) {
                append(expr.getSymbol())
                expr.getNext()
            }
        }.toInt()
    )

    private fun parseList(expr: StringHolder) = NestedList.JustList(
        buildList {
            while (!expr.isEnd() && expr.getSymbol() != ']') {
                expr.getNext()
                if (expr.getSymbol() == ']') {
                    break
                }
                add(parse(expr))
            }
            if (expr.getSymbol() == ']') {
                expr.getNext()
            }
        }
    )

    private fun parse(expr: StringHolder): NestedList = when (expr.getSymbol()) {
        in '0'..'9' -> parseNumber(expr)
        '[' -> parseList(expr)
        else -> error("unknown token ${expr.getSymbol()}")
    }

    fun parse(s: String) = parse(StringHolder(s))
}

class Day13 : Day {
    override fun part1(): Any {
        val pairs = readInput().chunked(3) {
            ListParser().parse(it[0]) to ListParser().parse(it[1])
        }
        /*.also {
            it.forEach {
                println(it)
            }
        }*/
        var sum = 0
        pairs.forEachIndexed { index, (a, b) ->
            if (a.compareTo(b) == -1)
                sum += index + 1
        }
        return sum
    }

    private fun makeDecoder(x: Int): ListParser.NestedList {
        return ListParser.NestedList.JustList(listOf(ListParser.NestedList.JustList(listOf(ListParser.NestedList.Const(x)))))
    }

    override fun part2(): Int {
        val pairs = (readInput().filter { it.isNotBlank() }
            .map { ListParser().parse(it) } + listOf(2, 6).map { makeDecoder(it) }).sorted()
//        pairs.forEach {
//            println(it)
//        }
        return (pairs.indexOfFirst{ it.compareTo(makeDecoder(2)) == 0} + 1) *
                (pairs.indexOfFirst{  it.compareTo(makeDecoder(6)) == 0} + 1)
    }

    private fun readInput() = File(INPUT_PATH).readLines()

    companion object {
                private const val INPUT_PATH = "day13.in"
//        private const val INPUT_PATH = "day13.in.exmp"
    }
}