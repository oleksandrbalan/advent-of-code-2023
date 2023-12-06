fun main() {
    val input = readInput("Day05")

    val almanac = Day5.parse(input)

    Day5.part1(almanac).println()
    Day5.part2(almanac).println()
}

private object Day5 {

    fun part1(almanac: Almanac): Long =
        almanac.seeds
            .minOf(almanac::getLocation)

    fun part2(almanac: Almanac): Long =
        almanac.seeds
            .windowed(2, 2)
            .map { (src, len) -> (src until src + len) }
            .minOf { seedRange ->
                seedRange.minOf(almanac::getLocation)
            }

    fun parse(input: List<String>): Almanac {
        val seeds = input.first().split(" ").mapNotNull { it.toLongOrNull() }

        val maps = input
            .splitBy { it == "" }
            .drop(1)
            .map { map ->
                val name = map.first()
                val ranges = map
                    .drop(1)
                    .map { range ->
                        val (dst, src, len) = range.split(" ").map { it.toLong() }
                        Range(src until src + len, dst - src)
                    }
                AlmanacMap(name, ranges)
            }

        return Almanac(seeds, maps)
    }

    data class Almanac(
        val seeds: List<Long>,
        val maps: List<AlmanacMap>,
    ) {
        fun getLocation(value: Long): Long =
            maps.fold(value) { acc, map -> map.map(acc) }
    }

    data class AlmanacMap(
        val name: String,
        val ranges: List<Range>,
    ) {
        fun map(value: Long): Long {
            val rangeOffset = ranges
                .firstOrNull { it.source.contains(value) }
                ?.offset
                ?: 0
            return value + rangeOffset
        }
    }

    data class Range(
        val source: OpenEndRange<Long>,
        val offset: Long,
    )
}