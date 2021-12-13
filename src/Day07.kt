import kotlin.math.abs

fun main() {
    fun solver(
        state: Map<Int, Int>,
        searchSpace: Collection<Int>,
        computeDistance: (Int, Int) -> Int
    ): Int {
        val distances = state.mapValues { (_, count) -> Pair(-1, count) }.toMutableMap()

        var minEMD = Integer.MAX_VALUE
        searchSpace.forEach { x ->
            distances.replaceAll { xLoc, (_, cnt) ->
                computeDistance(xLoc, x) to cnt
            }
            val currentEMD = distances.values.sumOf { (a, b) -> a * b }
            if (currentEMD < minEMD) {
                minEMD = currentEMD
            } else {
                return@forEach
            }
        }
        return minEMD
    }

    fun part1(input: Map<Int, Int>) = solver(input, input.keys) { a, b -> abs(a - b) }

    fun part2(input: Map<Int, Int>) = solver(input, (input.keys.first()..input.keys.last()).toList()) { a, b ->
        val dx = abs(a - b)
        dx * (dx + 1) / 2
    }

    val input = readInput("Day07").first()
    val state = input.split(",").map { it.toInt() }.groupingBy { it }.eachCount().toSortedMap()
    println(part1(state))
    println(part2(state))
}