fun main() {

    fun <T, K> Grouping<T, K>.eachCountLong(): Map<K, Long> =
        foldTo(mutableMapOf(), 0L) { acc, _ -> acc + 1 }

    class Polymer(initialState: String) {

        private val segments = initialState.windowed(2).groupingBy { it }.eachCountLong().toMutableMap()

        private val _charFrequencies = initialState.groupingBy { it }.eachCountLong().toMutableMap()
        val charFrequencies: Map<Char, Long>
            get() = _charFrequencies

        fun map(mappings: Map<String, String>) {
            segments.filterValues { it > 0 }.forEach { (s, freq) ->
                val insertion = mappings[s] ?: return@forEach
                segments.compute(s) { _, v -> v!! - freq }
                segments.compute(s[0] + insertion) { _, v -> (v ?: 0L) + freq }
                segments.compute(insertion + s[1]) { _, v -> (v ?: 0L) + freq }
                _charFrequencies.compute(insertion.single()) { _, v -> (v ?: 0L) + freq }
            }
        }
    }

    fun solver(template: String, mappings: Map<String, String>, steps: Int): Long {
        val polymer = Polymer(template)
        repeat(steps) {
            polymer.map(mappings)
        }
        val frequencies = polymer.charFrequencies.values
        return frequencies.maxOf { it } - frequencies.minOf { it }
    }

    fun part1(template: String, mappings: Map<String, String>) = solver(template, mappings, 10)
    fun part2(template: String, mappings: Map<String, String>) = solver(template, mappings, 40)

    val input = readInput("Day14")
    val template = input.first()
    val mappings = input.takeLastWhile { it.isNotEmpty() }
        .map { it.split("->") }
        .associate { (from, insertion) -> from.trim() to insertion.trim() }

    println(part1(template, mappings))
    println(part2(template, mappings))
}