import toolbox.Graph

fun main() {

    class TraversalPath<T>(
        val traversalList: MutableList<T> = mutableListOf(),
        val visitedCount: MutableMap<T, Int> = mutableMapOf()
    ) {

        fun init(graph: Graph<T>) {
            for (node in graph.adjacencyMap.keys) this.visitedCount[node] = 0
        }

        fun isNotVisited(node: T): Boolean = visitedCount[node] == 0

        fun markVisitedAndAddToTraversalList(node: T) {
            visitedCount[node] = visitedCount[node]!! + 1
            traversalList.add(node)
        }
    }

    fun <T> TraversalPath<T>.copyOf() = TraversalPath(
        this.traversalList.toMutableList(),
        this.visitedCount.toMutableMap()
    )

    fun reachDestination(node: String) = node == "end"
    val startingNode = "start"

    fun solver(graph: Graph<String>, allowVisitMoreThanOnce: (String, TraversalPath<String>) -> Boolean): Int {
        val visitedMap = TraversalPath<String>()
        visitedMap.init(graph)
        visitedMap.markVisitedAndAddToTraversalList(startingNode)

        val queue = ArrayDeque<TraversalPath<String>>()
        queue.add(visitedMap)

        var result = 0

        while (queue.isNotEmpty()) {
            val currentPath = queue.removeFirst()
            val currentNode = currentPath.traversalList.last()
            if (reachDestination(currentNode)) {
                result++
                continue
            }
            for (node in graph.adjacencyMap[currentNode]!!) {
                if (currentPath.isNotVisited(node) || allowVisitMoreThanOnce(node, currentPath)) {
                    val nextPath = currentPath.copyOf()
                    nextPath.markVisitedAndAddToTraversalList(node)
                    queue.add(nextPath)
                }
            }
        }

        return result
    }

    fun isLargeCave(node: String) = node.uppercase() == node
    fun isSmallCave(node: String) = node.lowercase() == node && node !in listOf("start", "end")

    fun part1(graph: Graph<String>) = solver(graph) { node, _ -> isLargeCave(node) }
    fun part2(graph: Graph<String>) = solver(graph) { node, path ->
        isLargeCave(node) ||
                (isSmallCave(node) && !path.visitedCount.any { (n, cnt) -> isSmallCave(n) && cnt > 1 })
    }

    val graph = Graph<String>()
    val input = readInput("Day12").map { it.split("-").take(2) }
    input.forEach { (from, to) ->
        graph.addEdge(from, to)
    }

    println(part1(graph))
    println(part2(graph))
}