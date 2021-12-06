fun main() {
    fun part1(input: List<String>): Int {
        return input.map { it.toInt() }
                .zipWithNext { a, b -> b > a }
                .count { it }
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toInt() }
                .windowed(3) { it.sum() }
                .zipWithNext { a, b -> b > a }
                .count { it }
    }

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
