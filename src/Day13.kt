import toolbox.Point

fun main() {

    class Board {
        private val points = mutableSetOf<Point>()

        val numOfPoints
            get() = points.size

        fun mark(point: Point) {
            this.points.add(point)
        }

        fun foldX(axis: Int) {
            this.points.filter { it.x >= axis }.forEach { point ->
                this.points.remove(point)
                this.points.add(point.copy(x = 2 * axis - point.x))
            }
        }

        fun foldY(axis: Int) {
            this.points.filter { it.y >= axis }.forEach { point ->
                this.points.remove(point)
                this.points.add(point.copy(y = 2 * axis - point.y))
            }
        }

        fun visualize() = buildString {
            val width = points.maxOf { it.x } + 1
            val height = points.maxOf { it.y } + 1
            for (y in 0 until height) {
                for (x in 0 until width) {
                    append(if (points.contains(Point(x, y))) '#' else ' ')
                }
                append('\n')
            }
        }
    }

    fun createBoard(points: List<Point>): Board {
        val board = Board()
        points.forEach { point ->
            board.mark(point)
        }
        return board
    }

    fun Board.fold(axisName: String, axis: String) {
        when (axisName) {
            "x" -> this.foldX(axis.toInt())
            "y" -> this.foldY(axis.toInt())
            else -> throw RuntimeException("Invalid axis name $axisName")
        }
    }

    fun part1(board: Board, operation: List<String>): Int {
        val (a, axis) = operation
        board.fold(a, axis)
        return board.numOfPoints
    }

    fun part2(board: Board, operations: List<List<String>>): String {
        operations.forEach { (a, axis) ->
            board.fold(a, axis)
        }
        return board.visualize()
    }

    val input = readInput("Day13")
    val points = input.takeWhile { it.isNotEmpty() }
        .map { s ->
            val (x, y) = s.split(",").map { it.toInt() }
            Point(x, y)
        }
    val operations = input.takeLastWhile { it.isNotEmpty() }
        .map { it.removePrefix("fold along ") }
        .map { it.split("=") }

    println(part1(createBoard(points), operations.first()))
    println(part2(createBoard(points), operations))
}
