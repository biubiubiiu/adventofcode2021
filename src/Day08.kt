fun main() {
    val number2Length = mapOf(
        0 to 6, 1 to 2, 2 to 5, 3 to 5, 4 to 4,
        5 to 5, 6 to 6, 7 to 3, 8 to 7, 9 to 6
    )

    fun part1(outputs: List<String>): Int {
        val uniqueLength = number2Length.filterValues { value ->
            number2Length.values.count { it == value } == 1
        }.values
        return outputs.count { it.length in uniqueLength }
    }

    fun part2(patterns: List<String>, outputs: List<String>): Int {
        /**
         * In the code below, use 'a' to 'g' to name each segment
         *
         *  aaaa
         * b    c
         * b    c
         *  dddd
         * e    f
         * e    f
         *  gggg
         */
        return patterns.chunked(10).zip(outputs.chunked(4)).sumOf { (pattern, output) ->
            val sortedPattern = pattern.map { it.toSortedSet() }
            val sortedOutput = output.map { it.toSortedSet() }

            // We already know that 1, 4, 7, and 8 each use a unique number of segments
            val one = sortedPattern.first { it.size == number2Length[1] } // 1
            val seven = sortedPattern.first { it.size == number2Length[7] } // 7
            val segmentA = seven.minus(one) // find out 'a'
            val segmentCF = one.intersect(seven)  // find out 'c' and 'f'

            val four = sortedPattern.first { it.size == 4 } // 4
            val segmentBD = four.minus(one) // find out 'b' and 'd'

            val nine = sortedPattern.filter { it.size == number2Length[9] } // 0, 6 or 9
                .filter { it.containsAll(segmentCF) } // 0 or 9
                .first { it.containsAll(segmentBD) } // 9
            val segmentG = nine.minus(segmentA).minus(segmentCF).minus(segmentBD) // find out 'g'

            val three = sortedPattern.filter { it.size == number2Length[3] } // 2, 3 or 5
                .first { it.containsAll(segmentCF) } // 3
            val segmentGD = three.minus(seven) // find out 'g' and 'd'
            val segmentD = segmentGD.minus(segmentG) // find out 'd'

            val eight = sortedPattern.first { it.size == number2Length[8] } // 8
            val segmentE = eight.minus(nine) // find out 'e'

            val two = sortedPattern.filter { it.size == number2Length[5] } // 2, 3 or 5
                .first { it.containsAll(segmentE) } // 2
            val five = sortedPattern.filter { it.size == number2Length[2] } // 2, 3, 5
                .first { !it.containsAll(three) && !it.containsAll(two) } // 5

            // remaining numbers
            val six = five.plus(segmentE)
            val zero = eight.minus(segmentD)

            val mapping = mapOf(
                zero to 0, one to 1, two to 2, three to 3, four to 4,
                five to 5, six to 6, seven to 7, eight to 8, nine to 9
            ) // feels like a three-year-old child when typing this line

            sortedOutput.map { mapping[it]!! }.reduce { a, b -> a * 10 + b }
        }
    }

    val input = readInput("Day08")
    val patterns = input.map { it.split("|").first().trim().split(" ") }.flatten()
    val outputs = input.map { it.split("|").last().trim().split(" ") }.flatten()

    println(part1(outputs))
    println(part2(patterns, outputs))
}