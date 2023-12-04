package day04

import day02.splitByColon
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException

fun String.splitByPipe(): Pair<String, String> {
    val parts = this.split("|").map(String::trim)
    return parts.first() to parts.last()
}

fun String.splitNumbers(): List<Long> =
    this.split(" ")
        .filterNot(String::isEmpty)
        .map(String::toLong)


fun List<Long>.isWinningNumber(number: Long) = this.contains(number)

suspend fun getWinningNumbers(s: String): List<Long> = coroutineScope {
    val (winningString, actualActual) = s.splitByColon()
        .last()
        .splitByPipe()
    val winning = async { winningString.splitNumbers() }
    val actual = actualActual.splitNumbers()
    return@coroutineScope actual.filter { winning.await().isWinningNumber(it) }
}

fun getScore(winningNumbers: List<Long>, score: Long = 0): Long {
    return when(winningNumbers.size) {
        0 -> score
        1 -> score + 1
        else -> getScore(winningNumbers.drop(1), score) * 2
    }
}

suspend fun main(): Unit = runBlocking(context = Dispatchers.Default) {
    val scratchers = File({}.javaClass.classLoader.getResource("day04.txt")?.file ?: throw FileNotFoundException())
        .readLines()
        .map { async {getWinningNumbers(it) } }
        .awaitAll()
        .map { async { getScore(it) } }
        .awaitAll()
        .sumOf { it }
        .also(::println)
}