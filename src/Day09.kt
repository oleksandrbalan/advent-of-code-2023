fun main() {
    val input = readInput("Day09")

    val numbers = Day9.parse(input)

    Day9.part1(numbers).println()
    Day9.part2(numbers).println()
}

private object Day9 {

    fun part1(input: List<List<Int>>): Int =
        input.sumOf { predictLast(it) }

    fun part2(input: List<List<Int>>): Int =
        input.sumOf { predictFirst(it) }

    fun predictLast(input: List<Int>): Int =
        if (input.isEmpty()) 0 else (input.last() + predictLast(diffs(input)))

    fun predictFirst(input: List<Int>): Int =
        if (input.isEmpty()) 0 else (input.first() - predictFirst(diffs(input)))

    fun diffs(input: List<Int>): List<Int> =
        input.windowed(2) { (prev, next) -> next - prev }

    fun parse(input: List<String>): List<List<Int>> =
        input.map { row -> row.split(" ").map { it.toInt() } }
}