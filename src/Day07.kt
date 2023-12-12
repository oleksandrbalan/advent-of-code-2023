fun main() {
    val input = readInput("Day07")

    Day7.part1(input).println()
    Day7.part2(input).println()
}

private object Day7 {

    fun part1(input: List<String>): Int =
        solve(parse(input, joker = false))

    fun part2(input: List<String>): Int =
        solve(parse(input, joker = true))

    fun solve(hands: List<Hand>): Int =
        hands
            .sorted()
            .withIndex()
            .fold(0) { acc, (index, hand) -> acc + (index + 1) * hand.bid }

    fun parse(input: List<String>, joker: Boolean): List<Hand> =
        input.map { parse(it, joker) }

    fun parse(input: String, joker: Boolean): Hand {
        val (cards, bid) = input.split(" ")
        return Hand(
            cards = cards,
            indices = calculateIndices(cards, joker),
            strength = calculateStrength(cards, joker),
            bid = bid.toInt(),
        )
    }

    private fun calculateIndices(cards: String, joker: Boolean): List<Int> =
        (if (joker) jokerLabels else labels).let { labels ->
            cards.map { labels.indexOf(it) }
        }

    private fun calculateStrength(cards: String, joker: Boolean): Strength {
        val map = mutableMapOf<Char, Int>()
            .withDefault { 0 }

        cards.forEach { card ->
            map.merge(card, 1, Int::plus)
        }

        val sorted = if (joker) {
            val count = map.remove('J') ?: 0
            map
                .values
                .sortedByDescending { it }
                .toMutableList()
                .also {
                    if (it.isNotEmpty()) {
                        it[0] += count
                    } else {
                        it.add(count)
                    }
                }
        } else {
            map
                .values
                .sortedByDescending { it }
        }

        return when {
            sorted[0] == 5 -> Strength.SAME_5
            sorted[0] == 4 -> Strength.SAME_4
            sorted[0] == 3 && sorted[1] == 2 -> Strength.FULL_HOUSE
            sorted[0] == 3 -> Strength.SAME_3
            sorted[0] == 2 && sorted[1] == 2 -> Strength.PAIR_2
            sorted[0] == 2 -> Strength.PAIR_1
            else -> Strength.HIGH_CARD
        }
    }

    data class Hand(
        val cards: String,
        val indices: List<Int>,
        val strength: Strength,
        val bid: Int,
    ) : Comparable<Hand> {

        override fun compareTo(other: Hand): Int {
            val compareStrength = strength.compareTo(other.strength)
            if (compareStrength != 0) {
                return compareStrength
            }
            return indices.zip(other.indices) { a, b -> a.compareTo(b) }
                .firstOrNull { it != 0 }
                ?: 0
        }
    }

    enum class Strength { HIGH_CARD, PAIR_1, PAIR_2, SAME_3, FULL_HOUSE, SAME_4, SAME_5 }

    private val labels = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    private val jokerLabels = listOf('J') + (labels - 'J')
}