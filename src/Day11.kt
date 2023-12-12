import kotlin.math.abs

fun main() {
    val input = readInput("Day11")

    val image = Day11.parse(input)

    Day11.part1(image).println()
    Day11.part2(image).println()
}

private object Day11 {

    fun part1(image: Image): Long =
        solve(image, 2)

    fun part2(image: Image): Long =
        solve(image, 1_000_000)

    fun parse(input: List<String>): Image {
        val galaxies = buildList {
            input.forEachIndexed { r, row ->
                row.forEachIndexed { c, char ->
                    if (char == '#') {
                        add(Position(r.toLong(), c.toLong()))
                    }
                }
            }
        }
        return Image(input.size, input[0].length, galaxies)
    }

    fun solve(image: Image, expansion: Int): Long {
        val newGalaxies = image.galaxies.map {
            val offsetRow = offset(image.emptyRows, it.r.toInt(), expansion)
            val offsetColumn = offset(image.emptyColumns, it.c.toInt(), expansion)
            Position(it.r + offsetRow, it.c + offsetColumn)
        }

        return buildList {
            repeat(newGalaxies.size) { i ->
                repeat(newGalaxies.size) { j ->
                    if (i > j) {
                        val a = newGalaxies[i]
                        val b = newGalaxies[j]
                        add(abs(a.c - b.c) + abs(a.r - b.r))
                    }
                }
            }
        }.sum()
    }

    fun offset(empty: List<Int>, value: Int, expansion: Int): Int {
        val index = empty.indexOfFirst { it > value }
            .takeIf { it >= 0 }
            ?: empty.size
        return index * (expansion - 1)
    }

    data class Image(
        val rows: Int,
        val columns: Int,
        val galaxies: List<Position>,
    ) {
        val emptyColumns = List(columns) { it }
            .filter { column ->
                val cells = List(rows) { Position(it.toLong(), column.toLong()) }
                cells.all { it !in galaxies }
            }
        val emptyRows = List(rows) { it }
            .filter { row ->
                val cells = List(columns) { Position(row.toLong(), it.toLong()) }
                cells.all { it !in galaxies }
            }
    }

    data class Position(
        val r: Long,
        val c: Long,
    )
}