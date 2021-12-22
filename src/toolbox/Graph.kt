/**
 * Taken from https://developerlife.com/2018/08/16/algorithms-in-kotlin-5/
 */

package toolbox

class Graph<T> {
    val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()
    val edgeWeights: HashMap<Pair<T, T>, Int> = HashMap()

    fun addEdge(sourceVertex: T, destinationVertex: T, weight: Int = 1, bidirectional: Boolean = true) {
        // Add edge to source vertex / node.
        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
        edgeWeights[sourceVertex to destinationVertex] = weight

        if (bidirectional) {
            // Add edge to destination vertex / node.
            adjacencyMap
                .computeIfAbsent(destinationVertex) { HashSet() }
                .add(sourceVertex)
            edgeWeights[destinationVertex to sourceVertex] = weight
        }
    }

    override fun toString() = buildString {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }
}