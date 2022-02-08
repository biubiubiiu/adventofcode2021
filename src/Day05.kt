import toolbox.Point
import kotlin.math.abs
import kotlin.math.max


private fun String.map2Point(): Point {
    val (x, y) = this.split(",").take(2).map { it.toInt() }
    return Point(x, y)
}

class Line(
    private val startPoint: Point,
    private val endPoint: Point
) {
    val isHorizontal: Boolean = startPoint.y == endPoint.y
    val isVertical: Boolean = startPoint.x == endPoint.x
    val isDiagonal: Boolean = abs(startPoint.y - endPoint.y) == abs(startPoint.x - endPoint.x)

    fun passThroughPoints(): List<Point> {
        if (!(isHorizontal || isVertical || isDiagonal)) {
            throw UnsupportedOperationException("Only horizontal, vertical and diagonal lines are considered")
        }
        val dy = endPoint.y - startPoint.y
        val dx = endPoint.x - startPoint.x
        val step = max(abs(dx), abs(dy))
        val stepX = dx / step
        val stepY = dy / step
        return (0..step).map {
            Point(startPoint.x + it * stepX, startPoint.y + it * stepY)
        }.toList()
    }

    companion object {
        fun from(s: String): Line {
            val (start, end) = s.split("->").take(2).map { it.trim() }
            return Line(start.map2Point(), end.map2Point())
        }
    }
}

fun part1(lines: List<Line>): Int {
    val hvLines = lines.filter { it.isHorizontal || it.isVertical }
    val points = hvLines.map { it.passThroughPoints() }.flatten()
    val pointsCount = points.groupingBy { it }.eachCount()
    return pointsCount.filterValues { it > 1 }.count()
}

fun part2(lines: List<Line>): Int {
    val hvLines = lines.filter { it.isHorizontal || it.isVertical || it.isDiagonal }
    val points = hvLines.map { it.passThroughPoints() }.flatten()
    val pointsCount = points.groupingBy { it }.eachCount()
    return pointsCount.filterValues { it > 1 }.count()
}

fun main() {
    val input = readInput("Day05")
    val lines = input.map { Line.from(it) }
    println(part1(lines))
    println(part2(lines))
}