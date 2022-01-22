import toolbox.cartesianProduct
import toolbox.indexOfFirstConsecutive
import kotlin.math.ceil
import kotlin.math.floor

fun main() {

    class SnailfishNumber(
        var value: Long,
        var depth: Int
    )

    fun copyOf(other: SnailfishNumber): SnailfishNumber = SnailfishNumber(other.value, other.depth)

    fun parse(formulaStr: String): List<SnailfishNumber> {
        val ret: MutableList<SnailfishNumber> = mutableListOf()
        var currentDepth = 0
        for (c in formulaStr) {
            currentDepth = when (c) {
                '[' -> currentDepth + 1
                ']' -> currentDepth - 1
                else -> currentDepth
            }
            val digit = c.digitToIntOrNull()?.toLong() ?: continue
            ret.add(SnailfishNumber(digit, currentDepth))
        }
        return ret
    }

    class SnailfishSolver {
        private val state: MutableList<SnailfishNumber> = mutableListOf()

        fun solve(formulas: List<List<SnailfishNumber>>): Long {
            formulas.forEach {
                add(it)
                reduce()
            }
            return getSum()
        }

        fun add(other: List<SnailfishNumber>) {
            val isEmptyBefore = state.isEmpty()
            state.addAll(other.map { copyOf(it) })
            state.takeIf { !isEmptyBefore }?.forEach { it.depth += 1 }
        }

        private fun reduce() {
            while (true) {
                if (explodeMaybe()) continue
                if (splitMaybe()) continue
                break
            }
        }

        private fun explodeMaybe(): Boolean {
            val idxExplosionLeft = state.indexOfFirstConsecutive { a, b -> a.depth == b.depth && a.depth > 4 }
            if (idxExplosionLeft == -1) {
                return false
            }

            // update
            val explosionLeft = state.removeAt(idxExplosionLeft)
            val explosionRight = state.removeAt(idxExplosionLeft)
            state.add(idxExplosionLeft, SnailfishNumber(0, explosionLeft.depth - 1))
            state.getOrNull(idxExplosionLeft - 1)?.let { it.value += explosionLeft.value }
            state.getOrNull(idxExplosionLeft + 1)?.let { it.value += explosionRight.value }
            return true
        }

        private fun splitMaybe(): Boolean {
            val idxSplit = state.indexOfFirst { it.value >= 10 }
            if (idxSplit == -1) {
                return false
            }

            // update
            val numberToSplit = state.removeAt(idxSplit)
            val partitionRight = ceil(numberToSplit.value / 2f).toLong()
            val partitionLeft = floor(numberToSplit.value / 2f).toLong()
            state.add(idxSplit, SnailfishNumber(partitionRight, numberToSplit.depth + 1))
            state.add(idxSplit, SnailfishNumber(partitionLeft, numberToSplit.depth + 1))
            return true
        }

        fun getSum(): Long {
            while (state.size > 1) {
                val idxPairToReduce = state.indexOfFirstConsecutive { a, b -> a.depth == b.depth }
                val first = state.removeAt(idxPairToReduce)
                val second = state.removeAt(idxPairToReduce)
                val reduced = SnailfishNumber(first.value * 3 + second.value * 2, first.depth - 1)
                state.add(idxPairToReduce, reduced)
            }
            return state.first().value
        }
    }

    fun part1(formulas: List<List<SnailfishNumber>>): Long {
        return SnailfishSolver().solve(formulas)
    }

    fun part2(formulas: List<List<SnailfishNumber>>): Long {
        return formulas.cartesianProduct(formulas)
            .filter { pair -> pair.first != pair.second }
            .map { it.toList() }
            .map { SnailfishSolver().solve(it) }
            .maxOf { it }
    }

    val formulas = readInput("Day18").map { parse(it) }
    println(part1(formulas))
    println(part2(formulas))
}