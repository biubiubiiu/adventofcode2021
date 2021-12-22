/**
 * Modified from https://www.atomiccommits.io/dijkstras-algorithm-in-kotlin
 */

package toolbox


fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val visited: MutableSet<T> = mutableSetOf() // a subset of vertices, for which we know the true distance

    val vertices = graph.adjacencyMap.keys
    val weights = graph.edgeWeights

    val delta = vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = vertices.associateWith { null }.toMutableMap()

    while (visited != vertices) {
        val v = delta
            .filterKeys { !visited.contains(it) }
            .minByOrNull { it.value }!!
            .key

        graph.adjacencyMap[v]!!.minus(visited).forEach { neighbor ->
            val newPath = delta.getValue(v) + weights.getValue(v to neighbor)

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = v
            }
        }

        visited.add(v)
    }

    return previous.toMap()
}

fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}