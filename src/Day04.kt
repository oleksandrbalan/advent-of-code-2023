import kotlin.math.pow

fun main() {
    val input = readInput("Day04")

    val games = Day4.parse(input)

    Day4.part1(games).println()
    Day4.part2(games).println()
}

private object Day4 {

    fun part1(games: List<Game>): Int =
        games.sumOf { if (it.points == 0) 0 else 2.0.pow(it.points - 1).toInt() }

    fun part2(games: List<Game>): Int {
        val points = games.associate { it.id to it.points }
        val copies = games.associate { it.id to 1 }.toMutableMap()

        points.forEach { (id, points) ->
            val gameCopies = requireNotNull(copies[id])
            repeat(points) {
                copies[id + it + 1] = requireNotNull(copies[id + it + 1]) + gameCopies
            }
        }

        return copies.values.sum()
    }

    fun parse(input: List<String>): List<Game> =
        input.map { line ->
            val (gameWithId, allNumbers) = line.split(":")
            val (winning, numbers) = allNumbers.split("|")
            Game(
                id = gameWithId.split(" ").last().toInt(),
                winning = winning.split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
                numbers = numbers.split(" ").filter { it.isNotEmpty() }.map { it.toInt() },
            )
        }

    data class Game(
        val id: Int,
        val winning: List<Int>,
        val numbers: List<Int>,
    ) {
        val points: Int = numbers.count { it in winning }
    }
}