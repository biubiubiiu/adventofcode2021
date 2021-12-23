fun main() {

    val digitMap = hashMapOf(
        '0' to "0000", '1' to "0001", '2' to "0010", '3' to "0011", '4' to "0100", '5' to "0101",
        '6' to "0110", '7' to "0111", '8' to "1000", '9' to "1001", 'A' to "1010", 'B' to "1011",
        'C' to "1100", 'D' to "1101", 'E' to "1110", 'F' to "1111"
    )

    class PacketReader(private val str: String) {

        var cursor = 0

        fun read(len: Int): String {
            val result = str.substring(cursor, cursor + len)
            cursor += len
            return result
        }
    }

    class Packet(private val bits: String, externalReader: PacketReader? = null) {

        val version: Int
        val type: Int

        val literals: MutableList<Long> = mutableListOf()
        val subPackets: MutableList<Packet> = mutableListOf()

        init {
            val reader = externalReader ?: PacketReader(bits)
            with(reader) {
                version = read(3).toInt(radix = 2)
                type = read(3).toInt(radix = 2)

                when (type) {
                    4 -> unpackLiterals()
                    else -> unpackSubpackets()
                }
            }
        }

        val allVersions: List<Int>
            get() = listOf(version) + subPackets.map { it.allVersions }.flatten()

        val value: Long
            get() = evaluateValue()

        fun PacketReader.unpackLiterals() {
            val literalStrings: MutableList<String> = mutableListOf()
            while (true) {
                val literalString = read(5)
                literalStrings.add(literalString.takeLast(4))
                if (literalString.startsWith("0")) {
                    break
                }
            }
            literals.add(literalStrings.joinToString("").toLong(radix = 2))
        }

        fun PacketReader.unpackSubpackets() {
            val lengthId = read(1)
            val lengthContent = when (lengthId) {
                "0" -> read(15).toInt(radix = 2) // total length in bits of the sub-packets
                "1" -> read(11).toInt(radix = 2) // number of sub-packets contained by this packet
                else -> throw Exception("Should not be reached")
            }
            when (lengthId) {
                "0" -> {
                    val currentCursor = cursor
                    while (cursor - currentCursor < lengthContent) {
                        subPackets.add(Packet(bits, this))
                    }
                }
                "1" -> repeat(lengthContent) {
                    subPackets.add(Packet(bits, this))
                }
            }
        }

        fun evaluateValue() = when (type) {
            0 -> subPackets.sumOf { it.value }
            1 -> subPackets.map { it.value }.reduce { acc, l -> acc * l }
            2 -> subPackets.minOf { it.value }
            3 -> subPackets.maxOf { it.value }
            4 -> literals.first()
            5 -> if (subPackets.first().value > subPackets.last().value) 1L else 0L
            6 -> if (subPackets.first().value < subPackets.last().value) 1L else 0L
            7 -> if (subPackets.first().value == subPackets.last().value) 1L else 0L
            else -> throw Exception("Should not be reached")
        }
    }

    val hexString = readInput("Day16").single()
    val binString = hexString.map { digitMap[it] }.joinToString("")
    println("part1: " + Packet(binString).allVersions.sum())
    println("part2: " + Packet(binString).value)
}