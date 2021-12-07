fun main() {
    val OPERATOR_MAP = mapOf(
            "forward" to Pair(1, 0),
            "down" to Pair(0, 1),
            "up" to Pair(0, -1)
    )

    fun Sequence<String>.map2pair(): Sequence<Pair<String, Int>> {
        return this.map { it.split(" ").take(2) }
                .map { (op, dis) -> Pair(op, dis.toInt()) }
    }

    fun part1(input: List<String>): Int {
        return input.asSequence()
                .map2pair()
                .map { (op, dis) ->
                    val (move_x, move_y) = requireNotNull(OPERATOR_MAP[op])
                    Pair(move_x * dis, move_y * dis)
                }
                .reduce { (move_x_a, move_y_a), (move_x_b, move_y_b) -> Pair(move_x_a + move_x_b, move_y_a + move_y_b) }
                .toList()
                .reduce { x, y -> x * y }
    }

    fun part2(input: List<String>): Int {
        var current_x = 0
        var current_y = 0
        var aim = 0

        input.asSequence()
                .map2pair()
                .map { (op, unit) ->
                    val (op_x, op_y) = requireNotNull(OPERATOR_MAP[op])
                    Triple(op_x, op_y, unit)
                }
                .forEach { (op_x, op_y, unit) ->
                    current_x += op_x * unit
                    current_y += if (op_x > 0) aim * unit else 0 // only dive when forward
                    aim += op_y * unit
                }

        return current_x * current_y
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

