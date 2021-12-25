import toolbox.Point2d
import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs
import kotlin.math.sign

private class TargetArea(str: String) {

    val topLeft: Point2d
    val bottomRight: Point2d

    init {
        val (xRangeStr, yRangeStr) = str.removePrefix("target area: ").split(", ")
        val (minXStr, maxXStr) = xRangeStr.removePrefix("x=").split("..")
        val (minYStr, maxYStr) = yRangeStr.removePrefix("y=").split("..")

        topLeft = Point2d(x = minXStr.toInt(), y = maxYStr.toInt())
        bottomRight = Point2d(x = maxXStr.toInt(), y = minYStr.toInt())
    }
}

private sealed class RelativePosition {
    object Approaching : RelativePosition()
    object Hit : RelativePosition()
    object Overfly : RelativePosition()
}

private fun Point2d.relation(target: TargetArea) = when {
    this.x in (target.topLeft.x..target.bottomRight.x)
            && this.y in (target.bottomRight.y..target.topLeft.y) -> RelativePosition.Hit
    // Note: this is a simplified judgement, which doesn't holds
    // when a part of target area is on the left side of starting point
    this.x > target.bottomRight.x || this.y < target.bottomRight.y -> RelativePosition.Overfly
    else -> RelativePosition.Approaching
}

private class Probe(private var vx: Int, private var vy: Int) {

    private val trajectory: MutableList<Point2d> = mutableListOf()
    val maxHeight get() = trajectory.maxOf { it.y }

    fun step(): Point2d {
        if (trajectory.size > 0) {
            vx -= vx.sign
            vy -= 1
        }

        val current = trajectory.lastOrNull() ?: Point2d(0, 0)
        val next = current.copy(x = current.x + vx, y = current.y + vy)
        trajectory.add(next)
        return next
    }
}

private class Simulator {

    fun simulate(probe: Probe, target: TargetArea): Int? {
        while (true) {
            val current = probe.step()
            return when (current.relation(target)) {
                RelativePosition.Approaching -> continue
                RelativePosition.Hit -> probe.maxHeight
                RelativePosition.Overfly -> null
            }
        }
    }
}

private fun solver(targetArea: TargetArea): List<Int> {
    val simulator = Simulator()
    val vxRange = min(targetArea.topLeft.x, 0)..max(targetArea.bottomRight.x, 0)
    val vyRange = min(targetArea.bottomRight.y, 0)..max(abs(targetArea.bottomRight.y), abs(targetArea.topLeft.y))

    val maxHeightsOfReaches: MutableList<Int> = mutableListOf()
    for (vx in vxRange) {
        for (vy in vyRange) {
            val maxHeight = simulator.simulate(probe = Probe(vx, vy), target = targetArea) ?: continue
            maxHeightsOfReaches.add(maxHeight)
        }
    }

    return maxHeightsOfReaches
}

fun main() {
    val input = readInput("Day17").single()
    val targetArea = TargetArea(input)

    println("part1: " + solver(targetArea).maxOf { it })
    println("part2: " + solver(targetArea).count())
}
