import kotlin.math.abs

fun main() {
    val input = readInput("Day03")

    val schematic = Day3.parse(input)

    Day3.part1(schematic).println()
    Day3.part2(schematic).println()
}

private object Day3 {

    fun part1(schematic: Schematic): Int =
        schematic.numbers
            .filter { number -> schematic.symbols.any { number.isPart(it) } }
            .sumOf { it.value }

    fun part2(schematic: Schematic): Int =
        schematic.symbols
            .filter { it.value == '*' }
            .associateWith { symbol -> schematic.numbers.filter { it.isPart(symbol) } }
            .filter { it.value.size == 2 }
            .entries
            .sumOf { it.value[0].value * it.value[1].value }

    fun parse(input: List<String>): Schematic {
        val numbers = mutableListOf<Number>()
        val symbols = mutableListOf<Symbol>()
        var buffer = ""

        fun addToBuffer(char: Char) {
            buffer += char
        }

        fun resetBuffer(indexLine: Int, indexChar: Int) {
            val value = buffer.toInt()
            numbers += Number(
                value = value,
                location = Location(
                    line = indexLine,
                    column = indexChar - buffer.length
                )
            )

            buffer = ""
        }

        fun addToSymbols(indexLine: Int, indexChar: Int, char: Char) {
            symbols += Symbol(
                value = char,
                location = Location(
                    line = indexLine,
                    column = indexChar
                )
            )
        }

        input.forEachIndexed { indexLine, line ->
            line.forEachIndexed { indexChar, char ->
                if (char.isDigit()) {
                    addToBuffer(char)
                } else {
                    if (buffer.isNotEmpty()) {
                        resetBuffer(indexLine, indexChar)
                    }

                    if (char != '.') {
                        addToSymbols(indexLine, indexChar, char)
                    }
                }
            }
            if (buffer.isNotEmpty()) {
                resetBuffer(indexLine, line.length)
            }
        }

        return Schematic(numbers, symbols)
    }

    data class Schematic(
        val numbers: List<Number>,
        val symbols: List<Symbol>,
    )

    data class Number(
        val value: Int,
        val location: Location,
    ) {
        val length = value.toString().length

        val columnRange: IntRange = (location.column - 1)..(location.column + length)

        fun isPart(symbol: Symbol): Boolean {
            val lineMatch = abs(location.line - symbol.location.line) <= 1
            val columnMatch = symbol.location.column in columnRange
            return lineMatch && columnMatch
        }
    }

    data class Symbol(
        val value: Char,
        val location: Location,
    )

    data class Location(
        val line: Int,
        val column: Int,
    )
}