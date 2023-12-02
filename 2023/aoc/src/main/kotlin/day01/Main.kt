package day01

import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.lang.IllegalStateException

typealias NumberMap = Pair<Long, Int>

fun String.extractNumber(): List<Long> =
    this.filter { it.isDigit() }
        .map { it.digitToInt().toLong() }

fun String.lineCalibration(): Long =
    (this.extractNumber().first() * 10 + this.extractNumber().last()).also(::println)

suspend fun readCalibration(file: File): Long = coroutineScope {
    file.readLines()
//        .map { async { it.lineCalibration() } }
        .map { async { some(it) } }
        .awaitAll()
        .sum()
}

suspend fun getBoundaryLiteralNumbers(s: String): Result<Pair<NumberMap, NumberMap>> = coroutineScope {
    val literalNumbers = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        .map { async { it.toNumber() to s.indexOf(it) } }
        .awaitAll()
        .filterNot { it.second == -1 }

    if (literalNumbers.isEmpty()) return@coroutineScope Result.failure(NoSuchElementException())

    val lowest = literalNumbers.minBy { it.second }
    val highest = literalNumbers.maxBy { it.second }

    Result.success(Pair(lowest, highest))
}

fun getBoundaryActualNumbers(s: String): Result<Pair<NumberMap, NumberMap>> {
    val numbers = s.foldIndexed(listOf<NumberMap>()) { index, acc, c ->
        if (c.isDigit()) acc + (c.digitToInt().toLong() to index) else acc
    }.filterNot { it.second == -1 }

    if (numbers.isEmpty()) return Result.failure(NoSuchElementException())

    val lowest = runCatching { numbers.minBy { it.second } }
    val highest = runCatching { numbers.maxBy { it.second } }

    return if (lowest.isSuccess)
        Result.success(Pair(lowest.getOrThrow(), highest.getOrThrow()))
    else Result.failure(NoSuchElementException())
}

fun getLowerBoundary(numberMaps: List<NumberMap>) = numberMaps.minBy { it.second }.first

fun getUpperBoundary(numberMaps: List<NumberMap>) = numberMaps.maxBy { it.second }.first

suspend fun some(s: String): Long = coroutineScope {
    val literalDef = async { getBoundaryLiteralNumbers(s) }
    val actualBoundaryResult = getBoundaryActualNumbers(s)

    if(actualBoundaryResult.isFailure) {
        val result = literalDef.await().getOrThrow()
        return@coroutineScope result.first.first * 10 + result.second.first
    }

    val literalResult = literalDef.await()

    if(literalResult.isFailure) {
        val result = actualBoundaryResult.getOrThrow()
        return@coroutineScope result.first.first * 10 + result.second.first
    }

    val (lowerActual, higerActual) = actualBoundaryResult.getOrThrow()
    val (lowerLiteral, higherLiteral) = literalResult.getOrThrow()
    val upper = async { getUpperBoundary(listOf(higerActual, higherLiteral)) }
    val lower = getLowerBoundary(listOf(lowerActual, lowerLiteral))
    lower * 10 + upper.await()
}

fun String.toNumber(): Long = when (this) {
    "one" -> 1
    "two" -> 2
    "three" -> 3
    "four" -> 4
    "five" -> 5
    "six" -> 6
    "seven" -> 7
    "eight" -> 8
    "nine" -> 9
    "zero" -> 0
    else -> throw IllegalStateException()
}


suspend fun main() = runBlocking(context = Dispatchers.Default) {
    val file = File({}.javaClass.classLoader.getResource("calib.txt")?.file ?: throw FileNotFoundException())
    val cal = readCalibration(file)
    println(cal)
}