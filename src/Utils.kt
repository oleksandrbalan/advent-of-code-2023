import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("input/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

inline fun <T> List<T>.splitBy(predicate: (T) -> Boolean): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var buffer = mutableListOf<T>()

    for (element in this) {
        if (predicate(element)) {
            result.add(buffer)
            buffer = mutableListOf()
        } else {
            buffer.add(element)
        }
    }

    result.add(buffer)

    return result
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
