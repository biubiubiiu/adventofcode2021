import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String): List<String> = File("src/resources", "$name.txt").readLines()

/**
 * Read matrices from the given input txt file.
 */
inline fun <reified T> readMatrices(
    lines: List<String>,
    separator: String = "",
    transform: (String) -> T
): List<Array<Array<T>>> {
    val result = mutableListOf<Array<Array<T>>>()

    val rows = mutableListOf<Array<T>>()
    lines.forEach { line ->
        if (line == separator) {
            result.add(rows.toTypedArray())
            rows.clear()
            return@forEach
        }
        val row = line.trim().split("\\s+".toRegex())
            .map(transform)
            .toTypedArray()
        rows.add(row)
    }

    if (rows.size > 0) {
        result.add(rows.toTypedArray())
        rows.clear()
    }

    return result
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
