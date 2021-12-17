fun main() {

    class Point(
        var value: Int = -1,
        val adjacentPoints: MutableList<Point> = mutableListOf()
    ) {
        val aboutToFlash get() = this.value > 9

        private var hasFlashed = false

        fun increment(refresh: Boolean = false) {
            if (refresh) {
                this.hasFlashed = false
            }
            if (!hasFlashed) {
                this.value++
            }
        }

        fun flash() {
            this.value = 0
            this.hasFlashed = true
            this.adjacentPoints.forEach { it.increment() }
        }
    }

    fun part1(points: Array<Point>): Int {
        var flashCount = 0
        repeat(100) {
            points.forEach { point -> point.increment(refresh = true) }
            while (points.indexOfFirst { it.aboutToFlash } != -1) {
                points.filter { it.aboutToFlash }.forEach { it.flash().also { flashCount++ } }
            }
        }
        return flashCount
    }

    fun part2(points: Array<Point>): Int {
        var loop = 0
        while (points.map { it.value }.toSet().size > 1) {
            points.forEach { point -> point.increment(refresh = true) }
            while (points.indexOfFirst { it.aboutToFlash } != -1) {
                points.filter { it.aboutToFlash }.forEach { it.flash() }
            }
            loop++
        }
        return loop
    }

    fun adjacentIndexes(idx: Int): List<Int> {
        val row = idx / 10
        val col = idx % 10
        return listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
            .map { (dr, dc) -> row + dr to col + dc }
            .filter { (nr, nc) -> nr in 0..9 && nc in 0..9 }
            .map { (r, c) -> r * 10 + c }
    }

    fun processData(): Array<Point> {
        val digits = readInput("Day11").joinToString("").toCharArray().map { it.digitToInt() }
        val points = digits.map { Point(it) }.toTypedArray()

        points.forEachIndexed { index, point ->
            for (adjacentIndex in adjacentIndexes(index)) {
                point.adjacentPoints.add(points[adjacentIndex])
            }
        }

        return points
    }

    println(part1(processData()))
    println(part2(processData()))
}