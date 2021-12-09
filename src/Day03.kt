fun main() {

    fun findCommonBit(input: List<String>, bit: Int): Int? {
        val numberOfBitOne = input.count { it[bit] == '1' }
        return when {
            numberOfBitOne * 2 > input.size -> 1
            numberOfBitOne * 2 < input.size -> 0
            else -> null
        }
    }

    fun filterPrefix(input: List<String>, bit_to_match: (List<String>, Int) -> Int): String {
        var prefix = ""
        val idxs = (input.indices).toMutableSet()
        var i = 0
        while (idxs.size > 1) {
            val bit = bit_to_match(input.filterIndexed { index, _ -> idxs.contains(index) }, i)
            prefix += bit.toString()
            idxs.removeAll { !input[it].startsWith(prefix) }
            i += 1
        }
        return input[idxs.first()]
    }

    fun part1(input: List<String>): UInt {
        val len = input.first().length
        var result = 0u
        (0 until len).forEach { idx ->
            val commonBit = findCommonBit(input, idx)!!
            result = (result shl 1) + commonBit.toUInt()
        }
        val mask = (1u shl len) - 1u
        return result * (result.inv() and mask)
    }

    fun part2(input: List<String>): Int {
        val common = filterPrefix(input) { list, idx ->
            findCommonBit(list, idx) ?: 1
        }
        val uncommon = filterPrefix(input) { list, idx ->
            1 - (findCommonBit(list, idx) ?: 1)
        }
        return Integer.parseInt(common, 2) * Integer.parseInt(uncommon, 2)
    }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

