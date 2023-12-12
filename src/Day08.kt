fun main() {
    val input = readInput("Day08")

    val network = Day8.parse(input)

    Day8.part1(network).println()
    Day8.part2(network).println()
}

private object Day8 {

    fun part1(network: Network): Int =
        solve(
            network = network,
            start = "AAA",
            end = { it == "ZZZ" },
        )

    fun part2(network: Network): Long =
        network.nodes.keys
            .filter { it.last() == 'A' }
            .map { nodeId ->
                solve(
                    network = network,
                    start = nodeId,
                    end = { it.last() == 'Z' },
                ).toLong()
            }
            .lcm()


    fun solve(
        network: Network,
        start: String,
        end: (String) -> Boolean,
    ): Int {
        var currentNodeId = start
        var steps = 0
        while (!end(currentNodeId)) {
            val currentStep = network.steps[steps % network.steps.size]
            currentNodeId = network.nodes.getValue(currentNodeId).let { node ->
                when (currentStep) {
                    Step.L -> node.left
                    Step.R -> node.right
                }
            }
            steps += 1
        }
        return steps
    }

    fun parse(input: List<String>): Network {
        val (stepsRaw, nodesRaw) = input.splitBy { it == "" }
        val steps = stepsRaw
            .first()
            .mapNotNull { char -> Step.entries.find { it.name == char.toString() } }
        return Network(
            steps = steps,
            nodes = parseNodes(nodesRaw)
        )
    }

    fun parseNodes(input: List<String>): Map<String, Node> =
        input
            .map { raw ->
                Node(
                    id = raw.substring(0, 3),
                    left = raw.substring(7, 10),
                    right = raw.substring(12, 15),
                )
            }
            .associateBy { it.id }

    data class Network(
        val steps: List<Step>,
        val nodes: Map<String, Node>,
    )

    data class Node(
        val id: String,
        val left: String,
        val right: String,
    )

    enum class Step { L, R }
}