fun main() {
    val input = readInput("Day02")

    val games = Day2.parse(input)

    Day2.part1(games).println()
    Day2.part2(games).println()
}

private object Day2 {

    fun part1(games: List<Game>): Int =
        games
            .filter { game -> game.turns.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 } }
            .sumOf { it.id }

    fun part2(games: List<Game>): Int =
        games.sumOf { game ->
            val redMax = game.turns.maxOf { it.red }
            val greenMax = game.turns.maxOf { it.green }
            val blueMax = game.turns.maxOf { it.blue }
            redMax * greenMax * blueMax
        }

    fun parse(input: List<String>): List<Game> =
        input.map {
            val (gameWithId, turns) = it.split(":")
            Game(
                id = gameWithId.split(" ")[1].toInt(),
                turns = parseTurns(turns.split(";"))
            )
        }

    private fun parseTurns(turns: List<String>): List<Turn> =
        turns.map {
            val cubes = it.split(",")
                .associate {
                    val (number, color) = it.trim().split(" ")
                    color to number.toInt()
                }
            Turn(
                red = cubes["red"] ?: 0,
                green = cubes["green"] ?: 0,
                blue = cubes["blue"] ?: 0,
            )
        }

    data class Game(
        val id: Int,
        val turns: List<Turn>
    )

    data class Turn(
        val red: Int,
        val green: Int,
        val blue: Int,
    )
}