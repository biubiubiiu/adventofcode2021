import java.util.LinkedList

fun main() {

    fun part1(mat: Array<IntArray>): Int {
        var result = 0
        for (r in mat.indices) {
            for (c in mat[0].indices) {
                if (r > 0 && mat[r][c] >= mat[r - 1][c]) continue
                if (r < mat.size - 1 && mat[r][c] >= mat[r + 1][c]) continue
                if (c > 0 && mat[r][c] >= mat[r][c - 1]) continue
                if (c < mat[0].size - 1 && mat[r][c] >= mat[r][c + 1]) continue
                result += (mat[r][c] + 1)
            }
        }
        return result
    }

    fun part2(mat: Array<IntArray>): Int {
        val basinSizes = mutableListOf<Int>()
        val visited = Array(mat.size) { BooleanArray(mat[0].size) { false } }
        val directions = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

        val queue = LinkedList<Pair<Int, Int>>()
        for (r in mat.indices) {
            for (c in mat[0].indices) {
                if (visited[r][c]) continue
                visited[r][c] = true
                if (mat[r][c] == 9) continue

                // start bfs
                queue.add(r to c)
                var size = 1
                while (queue.isNotEmpty()) {
                    val (cr, cc) = queue.pop()
                    directions.forEach { (dr, dc) ->
                        val nr = cr + dr
                        val nc = cc + dc
                        if (nr >= 0 && nr < mat.size && nc >= 0 && nc < mat[0].size && !visited[nr][nc]) {
                            visited[nr][nc] = true
                            if (mat[nr][nc] != 9) {
                                queue.add(nr to nc)
                                size += 1
                            }
                        }
                    }
                }
                // end bfs

                basinSizes.add(size)
                queue.clear()
            }
        }

        return basinSizes.apply { sortDescending() }.take(3).reduce { a, b -> a * b }
    }

    val input = readInput("Day09").map {
        it.toCharArray().map { c -> c.digitToInt() }.toIntArray()
    }.toTypedArray()
    println(part1(input))
    println(part2(input))
}