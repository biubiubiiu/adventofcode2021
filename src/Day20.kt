import toolbox.Point
import toolbox.cartesianProduct

fun main() {

    class Simulator(
        enhancementStr: String,
        matrixStr: List<String>
    ) {

        private val enhancementRule: MutableList<Boolean>
        private val lightPixels: MutableSet<Point>

        private var topLeft: Point
        private var bottomRight: Point

        private val kernelSize: Int = 3
        private var isSurroundingLightUp: Boolean = false

        init {
            enhancementRule = enhancementStr.map { it == '#' }.toMutableList()
            lightPixels = matrixStr.asSequence()
                .mapIndexed { row, s ->
                    s.mapIndexed { col, c ->
                        Point(row, col) to (c == '#')
                    }
                }
                .flatten()
                .filter { it.second }
                .map { it.first }
                .toMutableSet()

            topLeft = Point(0, 0)
            bottomRight = Point(matrixStr.size, matrixStr[0].length)
        }

        fun step() {
            pad()
            val next: MutableSet<Point> = mutableSetOf()
            for (x in topLeft.x..bottomRight.x) {
                for (y in topLeft.y..bottomRight.y) {
                    val number = computeNumber(Point(x, y))
                    val nextState = enhancementRule[number]
                    if (nextState) {
                        next.add(Point(x, y))
                    }
                }
            }
            if (!isSurroundingLightUp && enhancementRule.first()) {
                isSurroundingLightUp = true
            } else if (isSurroundingLightUp && !enhancementRule.last()) {
                isSurroundingLightUp = false
            }

            lightPixels.clear()
            lightPixels.addAll(next)
        }

        fun numOfLightPixels(): Int = lightPixels.count()

        private fun pad() {
            topLeft = topLeft.copy(x = topLeft.x - kernelSize, y = topLeft.y - kernelSize)
            bottomRight = bottomRight.copy(x = bottomRight.x + kernelSize, y = bottomRight.y + kernelSize)
        }

        private fun computeNumber(center: Point): Int {
            var ret = 0
            (-1..1).cartesianProduct(-1..1).forEach { (dx, dy) ->
                ret = ret shl 1
                val isLight = getLightState(Point(center.x + dx, center.y + dy))
                if (isLight) {
                    ret += 1
                }
            }
            return ret
        }

        private fun getLightState(point: Point): Boolean {
            val insideCentralArea = point.x in topLeft.x + kernelSize..bottomRight.x - kernelSize
                    && point.y in topLeft.y + kernelSize..bottomRight.y - kernelSize
            return if (insideCentralArea) {
                lightPixels.contains(point)
            } else {
                isSurroundingLightUp || lightPixels.contains(point)
            }
        }
    }

    class Solver(
        private val enhancementStr: String,
        private val matrixStr: List<String>
    ) {
        fun solve(repeatTimes: Int): Int {
            val simulator = Simulator(enhancementStr, matrixStr)
            repeat(repeatTimes) { simulator.step() }
            return simulator.numOfLightPixels()
        }
    }

    val input = readInput("Day20")
    val enhancementRule = input.first()
    val matrix = input.takeLastWhile { it.isNotEmpty() }

    val solver = Solver(enhancementRule, matrix)
    println("part1: ${solver.solve(repeatTimes = 2)}")
    println("part2: ${solver.solve(repeatTimes = 50)}")
}