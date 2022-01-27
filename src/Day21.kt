import kotlin.math.max
import kotlin.math.min

fun main() {

    class State(val pos: Int, val score: Int) {

        fun update(moveDistance: Int): State {
            val nextPos = (pos + moveDistance - 1) % 10 + 1
            val nextScore = score + nextPos
            return State(nextPos, nextScore)
        }
    }

    class AgentPart1(startPosA: Int, startPosB: Int) {

        var stateA = State(startPosA, 0)
        var stateB = State(startPosB, 0)
        var round = 0

        private val isEnded
            get() = stateA.score >= 1000 || stateB.score >= 1000

        fun loop() {
            var prevMoveDistance = -1
            do {
                val distance = if (prevMoveDistance == -1) 6 else (prevMoveDistance + 9) % 10
                if (round % 2 == 0) {
                    stateA = stateA.update(distance)
                } else {
                    stateB = stateB.update(distance)
                }
                prevMoveDistance = distance
                round++
            } while (!isEnded)
        }
    }

    class AgentPart2 {

        private val possibleRolls = (0 until 27).map {
            it.toString(radix = 3)
                .padStart(3, '0')
                .map { c -> c.digitToInt() + 1 }
                .sum()
        }.groupingBy { it }.eachCount()

        fun runDfs(startA: Int, startB: Int): Pair<Long, Long> {
            return countWinner(State(startA, 0), State(startB, 0), player = 'A')
        }

        private fun countWinner(stateA: State, stateB: State, player: Char): Pair<Long, Long> {
            if (stateA.score >= 21) {
                return 1L to 0L
            } else if (stateB.score >= 21) {
                return 0L to 1L
            }

            var winningTimesA = 0L
            var winningTimesB = 0L

            possibleRolls.forEach { (moveDistance, appearTimes) ->
                val (winsA, winsB) = if (player == 'A') {
                    val nextA = stateA.update(moveDistance)
                    countWinner(nextA, stateB, player = 'B')
                } else {
                    val nextB = stateB.update(moveDistance)
                    countWinner(stateA, nextB, player = 'A')
                }

                winningTimesA += winsA * appearTimes
                winningTimesB += winsB * appearTimes
            }

            return winningTimesA to winningTimesB
        }
    }

    fun part1(startA: Int, startB: Int): Int {
        val agent = AgentPart1(startA, startB)
        agent.loop()
        return agent.round * 3 * min(agent.stateA.score, agent.stateB.score)
    }

    fun part2(startA: Int, startB: Int): Long {
        val agent = AgentPart2()
        val (winsA, winsB) = agent.runDfs(startA, startB)
        return max(winsA, winsB)
    }

    val input = readInput("Day21")
    val (startA, startB) = input.map { it.split(' ').last().toInt() }

    println(part1(startA, startB))
    println(part2(startA, startB))
}