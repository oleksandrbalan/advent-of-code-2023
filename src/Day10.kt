import kotlin.math.abs
import kotlin.math.ceil

fun main() {
    val input = readInput("Day10")

    val grid = Day10.parse(input)

    Day10.part1(grid).println()
    Day10.part2(grid).println()
}

private object Day10 {

    fun part1(grid: Grid): Int =
        ceil(grid.mainLoopPositions.size / 2f).toInt()

    // Kudos to @Kroppeb 🤯
    // "It uses the shoelace method for the area and Pick's theorem for the interior points"
    fun part2(grid: Grid): Int {
        val perimeter = grid.mainLoopPositions.size
        val area = grid.mainLoopPositions
            .zip(grid.mainLoopPositions.drop(1) + grid.mainLoopPositions.take(1)) { a, b -> a.r * b.c - a.c * b.r }
            .sum()
            .let { abs(it) / 2 }
        return area - perimeter / 2 + 1
    }

    fun parse(input: List<String>): Grid {
        var start = Position(0, 0)

        val pipes = input.mapIndexed { r, row ->
            row.mapIndexed { c, char ->
                when (char) {
                    'S' -> {
                        start = Position(r, c)
                        null
                    }

                    in pipeValues -> {
                        Pipe(
                            value = char,
                            position = Position(r, c)
                        )
                    }

                    else -> null
                }
            }
        }

        return Grid(start, pipes)
    }

    data class Position(
        val r: Int,
        val c: Int,
    ) {
        operator fun plus(position: Position): Position =
            Position(r + position.r, c + position.c)

        fun top(): Position = Position(r - 1, c)
        fun right(): Position = Position(r, c + 1)
        fun bottom(): Position = Position(r + 1, c)
        fun left(): Position = Position(r, c - 1)
    }

    data class Pipe(
        val value: Char,
        val position: Position,
    ) {
        val a: Position
        val b: Position

        init {
            val (localA, localB) = when (value) {
                '|' -> Position(-1, +0) to Position(+1, +0)
                '-' -> Position(+0, -1) to Position(+0, +1)
                'L' -> Position(-1, +0) to Position(+0, +1)
                'J' -> Position(-1, +0) to Position(+0, -1)
                '7' -> Position(+1, +0) to Position(+0, -1)
                'F' -> Position(+1, +0) to Position(+0, +1)
                else -> error("Wrong pipe: $value")
            }
            a = position + localA
            b = position + localB
        }
    }

    data class Grid(
        val start: Position,
        val pipes: List<List<Pipe?>>,
    ) {
        val mainLoopPositions: List<Position> = buildList {
            add(start)

            var prev = start
            var curr = findStartingPipe()
            while (curr != start) {
                add(curr)
                val pipe = requireNotNull(pipes[curr.r][curr.c])
                curr = when (prev) {
                    pipe.a -> pipe.b
                    pipe.b -> pipe.a
                    else -> error("Broken pipe found")
                }
                prev = pipe.position
            }
        }

        fun isInBounds(position: Position): Boolean =
            position.r in pipes.indices && position.c in pipes[0].indices

        fun findStartingPipe(): Position {
            val neighbourPipes = listOfNotNull(
                start.top().takeIf(::isInBounds),
                start.right().takeIf(::isInBounds),
                start.bottom().takeIf(::isInBounds),
                start.left().takeIf(::isInBounds),
            ).associateWith {
                pipes[it.r][it.c]
            }

            return neighbourPipes.values
                .filterNotNull()
                .first { it.a == start || it.b == start }
                .position
        }
    }

    private const val pipeValues = "|-LJ7F"
}