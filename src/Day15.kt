import toolbox.Graph
import toolbox.dijkstra
import toolbox.shortestPath

fun main() {

    fun solver(repeatTimes: Int = 1): Int {
        data class Point(val index: Int)

        val input = readInput("Day15")
        val nrow = input.size
        val ncol = input.first().length

        val mat = input.map { it.toCharArray().map { c -> c.digitToInt() } }.flatten().toTypedArray()
        val points = List(nrow * ncol * repeatTimes * repeatTimes) { Point(it) }

        fun getMatValue(row: Int, col: Int): Int {
            val regionalRow = row / nrow
            val regionalCol = col / ncol
            val expandedValue = regionalRow + regionalCol
            return (mat[(row % nrow) * ncol + (col % ncol)] + expandedValue - 1) % 9 + 1
        }

        fun getPoint(row: Int, col: Int): Point {
            return points[row * ncol * repeatTimes + col]
        }

        val graph = Graph<Point>()
        for (i in 0 until nrow * repeatTimes) {
            for (j in 0 until ncol * repeatTimes) {
                if (i > 0) graph.addEdge(
                    getPoint(i - 1, j),
                    getPoint(i, j),
                    weight = getMatValue(i, j),
                    bidirectional = false
                )
                if (j > 0) graph.addEdge(
                    getPoint(i, j - 1),
                    getPoint(i, j),
                    weight = getMatValue(i, j),
                    bidirectional = false
                )
                if (i < nrow * repeatTimes - 1) graph.addEdge(
                    getPoint(i + 1, j),
                    getPoint(i, j),
                    weight = getMatValue(i, j),
                    bidirectional = false
                )
                if (j < ncol * repeatTimes - 1) graph.addEdge(
                    getPoint(i, j + 1),
                    getPoint(i, j),
                    weight = getMatValue(i, j),
                    bidirectional = false
                )
            }
        }

        val start = getPoint(0, 0)
        val destination = getPoint(nrow * repeatTimes - 1, ncol * repeatTimes - 1)
        val shortestPaths = dijkstra(graph, start)
        val path = shortestPath(shortestPaths, start, destination)
        return path.zipWithNext().sumOf { graph.edgeWeights.getValue(it) }
    }

    println("part1: " + solver(repeatTimes = 1))
    println("part2: " + solver(repeatTimes = 5))
}