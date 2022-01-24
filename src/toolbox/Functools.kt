package toolbox

fun <T> List<T>.indexOfFirstConsecutive(predicate: (T, T) -> Boolean): Int {
    return withIndex().zipWithNext().indexOfFirst { (a, b) ->
        predicate(a.value, b.value)
    }
}

fun <T, S> Iterable<T>.cartesianProduct(other: Iterable<S>): List<Pair<T, S>> {
    return cartesianProduct(other) { first, second -> first to second }
}

fun <T, S, V> Iterable<T>.cartesianProduct(other: Iterable<S>, transformer: (first: T, second: S) -> V): List<V> {
    return this.flatMap { first -> other.map { second -> transformer.invoke(first, second) } }
}