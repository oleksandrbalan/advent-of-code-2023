fun main() {
    val input = readInput("Day01")
    Day1.part1(input).println()
    Day1.part2(input).println()
}

private object Day1 {

    fun part1(input: List<String>): Int =
        input.sumOf { firstAndLastDigitNumber(it) }

    fun part2(input: List<String>): Int =
        input.sumOf {
            firstAndLastDigitNumber(
                input = it,
                mapFirst = ::digitizeFirst,
                mapLast = ::digitizeLast,
            )
        }

    private fun firstAndLastDigitNumber(
        input: String,
        mapFirst: String.() -> String = { input },
        mapLast: String.() -> String = { input },
    ): Int {
        val first = input.mapFirst().firstNotNullOfOrNull(Char::digitToIntOrNull)
        val last = input.mapLast().reversed().firstNotNullOfOrNull(Char::digitToIntOrNull)
        return if (first != null && last != null) {
            first * 10 + last
        } else {
            return 0
        }
    }

    private fun digitizeFirst(input: String): String =
        digitize(
            input = input,
            findIndex = { indexOf(it, ignoreCase = true) },
            findDigit = { minBy { it.value } },
        )

    private fun digitizeLast(input: String): String =
        digitize(
            input = input,
            findIndex = { lastIndexOf(it, ignoreCase = true) },
            findDigit = { maxBy { it.value } },
        )

    private fun digitize(
        input: String,
        findIndex: String.(String) -> Int,
        findDigit: Map<Digit, Int>.() -> Map.Entry<Digit, Int>
    ): String {
        val ids = Digit.entries
            .associateWith { input.findIndex(it.name) }
            .filter { it.value >= 0 }
        if (ids.isEmpty()) {
            return input
        }
        val digit = ids.findDigit()
        return input.replaceRange(
            startIndex = digit.value,
            endIndex = digit.value + digit.key.name.length,
            replacement = digit.key.ordinal.toString()
        )
    }

    private enum class Digit { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE; }
}