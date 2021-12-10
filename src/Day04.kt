class CheckerBoard(initState: Array<Array<Int>>) {
    private val state: IntArray // a 1d array to store checkerboard status
    private val rows: Int
    private val cols: Int

    private val rowWinningStates: IntArray
    private val colWinningStates: IntArray

    val winningStatus = mutableListOf<Boolean>()

    var stateSum: Int

    init {
        state = initState.flatten().toIntArray()
        rows = initState.size
        cols = initState[0].size
        stateSum = state.sum()

        rowWinningStates = IntArray(initState.size)
        colWinningStates = IntArray(initState[0].size)
    }

    val winning: Boolean
        get() = rowWinningStates.any { it == cols } || colWinningStates.any { it == rows }

    fun update(move: Int) {
        val idx = state.indexOf(move)
        if (idx == -1) {
            return
        }

        // update sum of unmarked numbers
        stateSum -= move

        // update winning state
        val row = idx / cols
        val col = idx % cols
        rowWinningStates[row] += 1
        colWinningStates[col] += 1

        // record status after each move
        if (winningStatus.lastOrNull() == true) { // skip if already wins
            return
        }
        winningStatus.add(winning)
    }
}

fun part1(moves: List<Int>, boards: List<Array<Array<Int>>>): Int {
    val checkerboards = boards.map { CheckerBoard(it) }
    val stopIndex = moves.indexOfFirst { move ->
        checkerboards.any { checkerboard ->
            checkerboard.update(move)
            checkerboard.winning
        }
    }
    val sum = checkerboards.first { it.winning }.stateSum
    return sum * moves[stopIndex]
}

fun part2(moves: List<Int>, boards: List<Array<Array<Int>>>): Int {
    val checkerboards = boards.map { CheckerBoard(it) }
    val stopIndex = moves.indexOfFirst { move ->
        checkerboards.filter { !it.winning }.forEach { it.update(move) }
        checkerboards.all { it.winning }
    }
    val sum = checkerboards.maxByOrNull { it.winningStatus.size }!!.stateSum
    return sum * moves[stopIndex]
}

fun main() {
    val input = readInput("Day04").toMutableList()
    val moves = input.take(1).first().split(",").map { it.toInt() }
    val checkerboards = readMatrices(input.drop(2)) { it.toInt() }

    println(part1(moves, checkerboards))
    println(part2(moves, checkerboards))
}
