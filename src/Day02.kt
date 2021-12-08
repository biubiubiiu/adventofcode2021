fun main() {
    val OPERATOR_MAP = mapOf(
        "forward" to Pair(1, 0),
        "down" to Pair(0, 1),
        "up" to Pair(0, -1)
    )

    fun Sequence<String>.map2OpsUnit(): Sequence<Triple<Int, Int, Int>> {
        return this.map { it.split(" ").take(2) }
            .map { (op, unit) ->
                val (op_x, op_y) = requireNotNull(OPERATOR_MAP[op])
                Triple(op_x, op_y, unit.toInt())
            }
    }

    fun part1(input: List<String>): Int {
        return input.asSequence()
            .map2OpsUnit()
            .map { (op_x, op_y, unit) -> Pair(op_x * unit, op_y * unit) }
            .reduce { (move_x_a, move_y_a), (move_x_b, move_y_b) ->
                Pair(move_x_a + move_x_b, move_y_a + move_y_b)
            }
            .let { it.first * it.second }
    }

    data class State(val x: Int, val y: Int, val aim: Int = -1)

    fun part2(input: List<String>): Int {
        var state = State(x = 0, y = 0, aim = 0)

        input.asSequence()
            .map2OpsUnit()
            .forEach { (op_x, op_y, unit) ->
                val (x, y, aim) = state
                val next_x = x + op_x * unit
                val next_y = y + if (op_x > 0) aim * unit else 0 // only dive when forward
                val next_aim = aim + op_y * unit
                state = state.copy(x = next_x, y = next_y, aim = next_aim)
            }

        return state.x * state.y
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}