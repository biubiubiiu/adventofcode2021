import java.util.Stack

fun main() {
    fun Char.isLeftBracket() = this in listOf('(', '[', '{', '<')

    val matchedPairs = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    fun Char.matchLeft(other: Char) = matchedPairs[other] == this

    val corruptedScore = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val incompleteScore = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    fun part1(inputs: List<CharArray>): Int {
        var result = 0
        val stack = Stack<Char>()
        inputs.forEach newRound@{ charArray ->
            stack.clear()
            charArray.forEach { c ->
                if (c.isLeftBracket()) {
                    stack.push(c)
                }
                // c is right bracket
                else if (stack.isNotEmpty() && c.matchLeft(stack.peek())) {
                    stack.pop()
                } else {
                    result += corruptedScore[c]!!  // unmatched character, corrupted
                    return@newRound
                }
            }
        }
        return result
    }

    fun part2(inputs: List<CharArray>): Long {
        val scores = mutableListOf<Long>()
        val stack = Stack<Char>()
        inputs.forEach newRound@{ charArray ->
            stack.clear()
            charArray.forEach { c ->
                if (c.isLeftBracket()) {
                    stack.push(c)
                }
                // c is right bracket
                else if (stack.isNotEmpty() && c.matchLeft(stack.peek())) {
                    stack.pop()
                } else {
                    return@newRound
                }
            }
            val missingBrackets = stack.map { matchedPairs[it] }.reversed()
            val score = missingBrackets.map { incompleteScore[it]!!.toLong() }.reduce { a, b -> 5 * a + b }
            scores.add(score)
        }
        return scores.sorted().elementAt(scores.size / 2)
    }

    val input = readInput("Day10").map { it.toCharArray() }
    println(part1(input))
    println(part2(input))
}