@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    fun update(state: ULongArray) {
        val newBorn = state[0]
        (0..7).forEach { i ->
            state[i] = state[i + 1]
        }
        state[8] = newBorn
        state[6] += newBorn
    }

    fun part1(state: ULongArray): ULong {
        repeat(80) { update(state) }
        return state.sum()
    }

    fun part2(state: ULongArray): ULong {
        repeat(256) { update(state) }
        return state.sum()
    }

    val input = readInput("Day06").first().split(",").map { it.toInt() }
    val count = input.groupingBy { it }.eachCount()
    val state = ULongArray(9) { count.getOrDefault(it, 0).toULong() }
    println(part1(state.copyOf()))
    println(part2(state.copyOf()))
}