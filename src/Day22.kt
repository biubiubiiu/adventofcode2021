import kotlin.math.min
import kotlin.math.max

fun main() {

    class Cuboid(
        val xRange: LongRange,
        val yRange: LongRange,
        val zRange: LongRange,
    ) {
        val volume: Long
            get() = listOf(xRange, yRange, zRange).map { it.last - it.first + 1 }.reduce { a, b -> a * b }
    }

    fun Cuboid.intersect(other: Cuboid): Cuboid? {
        val overlapping = listOf(xRange, yRange, zRange).zip(listOf(other.xRange, other.yRange, other.zRange))
            .map { (a, b) ->
                val start = max(a.first, b.first)
                val end = min(a.last, b.last)
                if (start <= end) start..end else null
            }
        val intersectX = overlapping[0] ?: return null
        val intersectY = overlapping[1] ?: return null
        val intersectZ = overlapping[2] ?: return null
        return Cuboid(intersectX, intersectY, intersectZ)
    }

    data class Operation(
        val isOn: Boolean,
        val cuboid: Cuboid
    )

    fun solve(operations: List<Operation>): Long {
        val finalCuboids: MutableList<Pair<Cuboid, Boolean>> = mutableListOf()
        for (op in operations) {
            val cuboidsToAdd: MutableList<Pair<Cuboid, Boolean>> = mutableListOf()
            for ((cuboid, isOnn) in finalCuboids) {
                val intersection = op.cuboid.intersect(cuboid) ?: continue
                val nextIsOffState = when (op.isOn to isOnn) {
                    true to true -> false
                    false to false -> true
                    else -> op.isOn
                }
                cuboidsToAdd.add(intersection to nextIsOffState)
            }
            if (op.isOn) {
                cuboidsToAdd.add(op.cuboid to true)
            }
            finalCuboids.addAll(cuboidsToAdd)
        }

        val result = finalCuboids.fold(0L) { acc, (c, isOn) ->
            acc + c.volume * (if (isOn) 1 else -1)
        }
        return result
    }

    fun Cuboid.constraintInCube(range: LongRange): Cuboid? {
        if (listOf(xRange.last, yRange.last, zRange.last).any { it < range.first }) {
            return null
        }
        if (listOf(xRange.first, yRange.first, zRange.first).any { it > range.last }) {
            return null
        }
        return Cuboid(
            xRange = max(xRange.first, -50)..min(xRange.last, 50),
            yRange = max(yRange.first, -50)..min(yRange.last, 50),
            zRange = max(zRange.first, -50)..min(zRange.last, 50)
        )
    }

    fun part1(operations: List<Operation>) = solve(operations.map { it.isOn to it.cuboid }
        .map { (isOff, c) -> isOff to c.constraintInCube(-50L..50L) }
        .filter { (_, c) -> c != null }
        .map { (isOff, c) -> Operation(isOff, c!!) }
    )

    fun part2(operations: List<Operation>) = solve(operations)

    fun process(s: String): Operation {
        val (type, ranges) = s.split(" ")
        val (xRange, yRange, zRange) = ranges.split(",").map { it.substring(2) }
        val (startX, endX) = xRange.split("..").map { it.toLong() }
        val (startY, endY) = yRange.split("..").map { it.toLong() }
        val (startZ, endZ) = zRange.split("..").map { it.toLong() }
        return Operation(
            isOn = type == "on",
            cuboid = Cuboid(startX..endX, startY..endY, startZ..endZ)
        )
    }

    val input = readInput("Day22").map(::process)
    println(part1(input))
    println(part2(input))
}