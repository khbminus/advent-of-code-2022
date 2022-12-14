fun <T> List<T>.splitBy(elem: T): List<List<T>> {
    if (isEmpty()) {
        return emptyList()
    }
    val transformed: List<MutableList<T>> = buildList {
        add(mutableListOf())
        this@splitBy.forEach {
            when (it) {
                elem -> add(mutableListOf())
                else -> last().add(it)
            }
        }
    }
    return transformed
}

operator fun Pair<Int, Int>.minus(oth: Pair<Int, Int>) = (first - oth.first) to (second - oth.second)
operator fun Pair<Int, Int>.plus(oth: Pair<Int, Int>) = (first + oth.first) to (second + oth.second)
