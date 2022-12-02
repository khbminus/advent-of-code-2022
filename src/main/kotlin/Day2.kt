import java.io.File

class Day2 {
    enum class ChosenMove {
        ROCK, SCISSORS, PAPER
    }

    enum class GameResult {
        Win, Draw, Lose
    }

    private fun readInput(): List<String> = File(INPUT_PATH)
        .readLines()

    private fun convertMove(x: String): ChosenMove = when (x) {
        "A" -> ChosenMove.ROCK
        "B" -> ChosenMove.PAPER
        "C" -> ChosenMove.SCISSORS

        "X" -> ChosenMove.ROCK
        "Y" -> ChosenMove.PAPER
        "Z" -> ChosenMove.SCISSORS
        else -> error("Invalid letter")
    }

    private fun convertStrategy(x: String): GameResult = when (x) {
        "X" -> GameResult.Lose
        "Y" -> GameResult.Draw
        "Z" -> GameResult.Win
        else -> error("Invalid letter")
    }

    private fun chooseLose(a: ChosenMove) = when (a) {
        ChosenMove.PAPER -> ChosenMove.ROCK
        ChosenMove.SCISSORS -> ChosenMove.PAPER
        ChosenMove.ROCK -> ChosenMove.SCISSORS
    }

    private fun winScore(a: ChosenMove, b: ChosenMove) = if (a == chooseLose(b)) WIN_SCORE else 0

    private fun chooseDraw(a: ChosenMove) = a
    private fun drawScore(a: ChosenMove, b: ChosenMove) = if (a == chooseDraw(b)) TIE_SCORE else 0

    private fun chooseWin(a: ChosenMove) = when (a) {
        ChosenMove.PAPER -> ChosenMove.SCISSORS
        ChosenMove.SCISSORS -> ChosenMove.ROCK
        ChosenMove.ROCK -> ChosenMove.PAPER
    }

    private fun gameOutcome(game: Pair<ChosenMove, ChosenMove>): Int {
        val (enemy, you) = game
        return drawScore(enemy, you) + winScore(enemy, you) + when (you) {
            ChosenMove.ROCK -> 1
            ChosenMove.PAPER -> 2
            ChosenMove.SCISSORS -> 3
        }
    }

    private fun makeGame(game: Pair<ChosenMove, GameResult>) = game.first to when (game.second) {
        GameResult.Win -> chooseWin(game.first)
        GameResult.Draw -> chooseDraw(game.first)
        GameResult.Lose -> chooseLose(game.first)
    }

    fun part1() = readInput()
        .map {
            val strategy = it.split(" ").map { move -> convertMove(move) }
            strategy[0] to strategy[1]
        }
        .sumOf { gameOutcome(it) }

    fun part2() = readInput()
        .map {
            val strategy = it.split(" ")
            convertMove(strategy[0]) to convertStrategy(strategy[1])
        }
        .sumOf { gameOutcome(makeGame(it)) }

    companion object {
        const val INPUT_PATH = "day2.in"
//        const val INPUT_PATH = "day2.in.exmp"
        const val WIN_SCORE = 6
        const val TIE_SCORE = 3
    }
}