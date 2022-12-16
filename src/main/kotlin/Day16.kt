import java.io.File

class Day16 : Day {

    private data class State(val mins: Int, val mask: Int, val myPosition: Int, val elephantPosition: Int) {
        fun open(pos: Int, tick: Int = 1): State {
            return copy(mask = mask.setBit(pos), mins = mins - tick)
        }

        fun moveElephant(pos: Int): State {
            return copy(elephantPosition = pos)
        }

        fun moveMe(pos: Int): State {
            return copy(mins = mins - 1, myPosition = pos)
        }

        override fun toString(): String {
            return "State(mins=$mins, mask=${mask.toString(2)}, myPosition=$myPosition, elephantPosition=$elephantPosition)"
        }
    }


    private val cache = mutableMapOf<State, Int>()
    private var matrixAvailable: List<List<Int>>
    private var pressures: MutableList<Int>
    private val valueNonZero: List<Int>
    private fun go(state: State): Int {
        if (state.mins == 0) {
            return 0
        }
        require(state.mins > 0) { "${state.mins} < 0" }
        if (cache.contains(state)) {
            return cache[state]!!
        }
        var maxValue = 0
        if (valueNonZero[state.myPosition] != -1 && state.mask.getBit(valueNonZero[state.myPosition]) == 0) {
            maxValue =
                go(state.open(valueNonZero[state.myPosition])) + pressures[state.myPosition] * (state.mins - 1)
        }
        maxValue = maxValue.coerceAtLeast(matrixAvailable[state.myPosition].maxOf {
            go(state.moveMe(it))

        })
        cache[state] = maxValue
        return maxValue
    }

    private fun go2(state: State): Int {
        if (state.mins == 0) {
            return 0
        }
        require(state.mins > 0) { "${state.mins} < 0" }
        val makeElephantMoves = { state: State ->
            var maxValue = 0
            if (valueNonZero[state.elephantPosition] != -1 && state.mask.getBit(valueNonZero[state.elephantPosition]) == 0) {
                maxValue =
                    go2(
                        state.open(
                            valueNonZero[state.elephantPosition],
                            tick = 0
                        )
                    ) + pressures[state.elephantPosition] * state.mins
            }
            maxValue.coerceAtLeast(matrixAvailable[state.elephantPosition].maxOf {
                go2(state.moveElephant(it))
            })
        }
        if (cache.contains(state)) {
            return cache[state]!!
        }
        var maxValue = 0
        if (valueNonZero[state.myPosition] != -1 && state.mask.getBit(valueNonZero[state.myPosition]) == 0) {
            maxValue =
                makeElephantMoves(state.open(valueNonZero[state.myPosition])) +
                        pressures[state.myPosition] * (state.mins - 1)
        }
        maxValue = maxValue.coerceAtLeast(matrixAvailable[state.myPosition].maxOf {
            makeElephantMoves(state.moveMe(it))

        })
        cache[state] = maxValue
        return maxValue
    }

    init {
        val vertexes = readInput().sortedBy { it.name }
        val indexes = vertexes.map { it.name }
        pressures = vertexes.map { it.rate }.toMutableList()
        vertexes.forEach {
            it.name = indexes.indexOf(it.name)
            it.adjacent = it.adjacent.map { indexes.indexOf(it).also { require(it >= 0) } }
        }
        matrixAvailable = MutableList(vertexes.size) { i ->
            vertexes[i].adjacent
        }
        var nonZero = 0
        valueNonZero = List(vertexes.size) {
            if (pressures[it] == 0) -1 else nonZero++
        }
    }

    data class Vertex(var name: Int, val rate: Int, var adjacent: List<Int>)

    private fun String.convertNameToInt(): Int = (this[0] - 'A') * 26 + (this[1] - 'A')
    private fun readInput() = File(INPUT_PATH).readLines().map {
        val (name, rate, adjacent) = INPUT_REGEX.matchEntire(it)?.destructured ?: error("Failed to match $it")
        Vertex(
            name.convertNameToInt(),
            rate.toInt(),
            adjacent.split(", ").map { it.convertNameToInt() }
        )
    }


    companion object {
        //        private const val INPUT_PATH = "day16.in.exmp"
        private const val INPUT_PATH = "day16.in"
        private val INPUT_REGEX =
            Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (([A-Z]{2}, )*([A-Z]{2})+)")
    }

    override fun part1(): Any = go(State(30, 0, 0, 0))

    override fun part2(): Any {
        cache.clear()
        return go2(State(26, 0, 0, 0))
    }
}

fun main() {
    println(Day16().part1())
    println(Day16().part2())
}