import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

fun main() {
    val input = readInput("Day06")

    Day6.part1(input).println()
    Day6.part2(input).println()
}

private object Day6 {

    fun part1(input: List<String>): Long {
        val times = input.first().getLongs()
        val distances = input.last().getLongs()
        return times
            .zip(distances) { time, distance -> solve(time, distance) }
            .fold(1L) { acc, value -> acc * value }
    }

    fun part2(input: List<String>): Long {
        val time = input.first().getLongIgnoringSpaces()
        val distance = input.last().getLongIgnoringSpaces()
        return solve(time, distance)
    }

    private fun solve(time: Long, distance: Long): Long {
        // Equation: x * x - time * x + distance > 0
        val discriminant = time * time - 4 * distance
        val sqrtDiscriminant = sqrt(discriminant.toDouble())
        val x1 = (time - sqrtDiscriminant) / 2.0
        val x2 = (time + sqrtDiscriminant) / 2.0
        return getBetween(x1, x2)
    }

    private fun getBetween(x1: Double, x2: Double): Long {
        val from = if (x1.rem(1) == 0.0) x1 + 1 else ceil(x1)
        val to = if (x2.rem(1) == 0.0) x2 - 1 else floor(x2)
        return to.toLong() - from.toLong() + 1
    }

    private fun String.getLongs(): List<Long> =
        split(" ").mapNotNull { it.toLongOrNull() }

    private fun String.getLongIgnoringSpaces(): Long =
        split(":")[1].replace(" ", "").toLong()
}