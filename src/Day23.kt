import toolbox.cartesianProduct
import java.util.PriorityQueue
import kotlin.math.abs

fun main() {

    data class AmphipodWithLocation(
        val symbol: Char,
        val index: Int,
        val depth: Int,  // hallway: 0; room: negative
        val destIndex: Int,
    )

    data class State(
        val hallway: Set<AmphipodWithLocation>,
        val rooms: Set<AmphipodWithLocation>
    )

    class StateWithCost(
        val state: State,
        val cost: Int
    ) : Comparable<StateWithCost> {
        override fun compareTo(other: StateWithCost): Int = cost.compareTo(other.cost)
    }

    class Agent {

        private val symbols = listOf('A', 'B', 'C', 'D')
        private val coeffs = listOf(1, 10, 100, 1000)
        private val destinationIndexes = listOf(2, 4, 6, 8)

        private val multipliers = symbols.zip(coeffs).toMap()
        private val destinations = symbols.zip(destinationIndexes).toMap()

        private var initialState: State? = null
        private var roomDepth: Int = -1

        fun setup(input: List<String>) {
            val hallway: Set<AmphipodWithLocation> = emptySet()  // hallway is always empty at first
            val rooms: MutableSet<AmphipodWithLocation> = mutableSetOf()

            input.drop(2).dropLast(1).forEachIndexed { row, line ->
                line.withIndex()
                    .filter { it.value.isLetter() }
                    .forEach { (col, c) ->
                        rooms.add(
                            AmphipodWithLocation(
                                c,
                                col - 1,
                                -row - 1,
                                destinations[c]!!,
                            )
                        )
                    }
            }

            initialState = State(hallway, rooms)
            roomDepth = rooms.size / 4
        }

        fun solve(): Int {
            val state = initialState ?: throw IllegalStateException("agent has not been setup")
            val toVisit = PriorityQueue<StateWithCost>().apply { add(StateWithCost(state, 0)) }
            val visited = mutableSetOf<StateWithCost>()
            val currentCosts = mutableMapOf<State, Int>().withDefault { Int.MAX_VALUE }

            while (toVisit.isNotEmpty()) {
                val current = toVisit.poll().also { visited.add(it) }
                getNextStates(current).forEach { next ->
                    if (!visited.contains(next)) {
                        val newCost = current.cost + next.cost
                        if (newCost < currentCosts.getValue(next.state)) {
                            currentCosts[next.state] = newCost
                            toVisit.add(StateWithCost(next.state, newCost))
                        }
                    }
                }
            }

            return currentCosts.keys.first { isFinished(it) }.let { currentCosts.getValue(it) }
        }

        fun getNextStates(curr: StateWithCost): List<StateWithCost> {
            val result: MutableList<StateWithCost> = mutableListOf()

            curr.state.hallway.filter { canMoveToDestination(curr.state, it) }
                .forEach { apod ->
                    val targetDepth = -roomDepth + curr.state.rooms.filter { it.index == apod.destIndex }.size
                    val moveDistance = abs(apod.index - apod.destIndex) + abs(targetDepth - apod.depth)
                    val cost = moveDistance * multipliers[apod.symbol]!!

                    val nextHallway = curr.state.hallway.toMutableSet().also { it.remove(apod) }
                    val nextRooms = curr.state.rooms.toMutableSet().also {
                        it.add(apod.copy(index = apod.destIndex, depth = targetDepth))
                    }
                    val nextState = StateWithCost(State(nextHallway, nextRooms), cost)
                    result.add(nextState)
                }

            curr.state.rooms.groupBy { it.index }
                .filter { (roomIndex, apods) -> apods.any { it.destIndex != roomIndex } }
                .mapValues { it.value.maxByOrNull { apod -> apod.depth }!! }
                .values
                .cartesianProduct(legalHallwayIndexes((curr.state)))
                .filter { (apod, targetIdx) -> canMoveToIndex(curr.state, apod, targetIdx) }
                .forEach { (apod, targetIndex) ->
                    val targetDepth = 0
                    val moveDistance = abs(apod.index - targetIndex) + abs(targetDepth - apod.depth)
                    val cost = moveDistance * multipliers[apod.symbol]!!

                    val nextHallway = curr.state.hallway.toMutableSet().also {
                        it.add(apod.copy(index = targetIndex, depth = targetDepth))
                    }
                    val nextRooms = curr.state.rooms.toMutableSet().also { it.remove(apod) }
                    val nextState = StateWithCost(State(nextHallway, nextRooms), cost)
                    result.add(nextState)
                }

            return result
        }

        private fun isFinished(state: State): Boolean {
            return state.hallway.isEmpty() && state.rooms.all { it.index == it.destIndex }
        }

        private fun canMoveToDestination(state: State, apod: AmphipodWithLocation): Boolean {
            val canEnterRoom = state.rooms.filter { it.index == apod.destIndex }.all { it.symbol == apod.symbol }
            val isHallwayPathClear = canMoveToIndex(state, apod, apod.destIndex)
            return canEnterRoom && isHallwayPathClear
        }

        private fun canMoveToIndex(state: State, apod: AmphipodWithLocation, destIndex: Int): Boolean {
            val path = when {
                apod.index < destIndex -> apod.index + 1..destIndex
                apod.index > destIndex -> apod.index - 1 downTo destIndex
                else -> return true
            }
            return !state.hallway.any { it.index in path }
        }

        private fun legalHallwayIndexes(state: State): Set<Int> {
            val indexes = mutableSetOf(0, 1, 3, 5, 7, 9, 10)
            val occupiedIndexes = state.hallway.map { it.index }
            occupiedIndexes.forEach {
                indexes.remove(it)
            }
            return indexes
        }
    }

    val input = readInput("Day23").toMutableList()
    val agent = Agent()

    // part1
    agent.setup(input)
    println(agent.solve())

    // part2
    input.addAll(3, listOf("  #D#C#B#A#", "  #D#B#A#C#"))
    agent.setup(input)
    println(agent.solve())
}