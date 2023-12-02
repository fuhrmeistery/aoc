package day02

import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import kotlin.collections.Map.Entry

fun String.splitByColon(): List<String> = this.split(":")

fun String.getId(): Long = this.split(" ").last().toLong()

fun String.drawsAsList(): List<String> = this.trim().split(";").map { it.trim() }

enum class Color(val value: String) {
    RED("red"),
    GREEN("green"),
    BLUE("blue")
}

fun String.splitColors(): List<String> = this.split(", ")

fun String.mapToBalls(): Balls {
    val countString = this.split(" ")
    val color = Color.valueOf(countString.last().uppercase())
    return Balls(color, countString.first().toLong())
}

fun List<Balls>.findMaxBallsPerColor(): Map<Color, Long> =
    this.groupBy(Balls::color)
        .mapValues { it.value.maxOf(Balls::count) }

fun String.isGamePossible(): Boolean {
    val gameIdGames =
        this.splitByColon()
            .map(String::trim)

    val game = gameIdGames.last()
    val rounds = game.drawsAsList()

    val maxBallsByColor = rounds
        .map(String::splitColors)
        .map { it.map(String::mapToBalls).findMaxBallsPerColor() }


    return maxBallsByColor.all { it.isDrawPossible() }
}

fun String.minPossibleSet(): Map<Color, Long> {
    val gameIdGames =
        this.splitByColon()
            .map(String::trim)

    val game = gameIdGames.last()
    val rounds = game.drawsAsList()

    return rounds
        .asSequence()
        .map(String::splitColors)
        .map { it.map(String::mapToBalls) }
        .flatten()
        .groupBy { it.color }
        .map { it.key to it.value.maxOf(Balls::count) }
        .toList()
        .toMap()

}

fun Map<Color, Long>.power(): Long {
    return this.map { it.value }.fold(1) { acc: Long, i -> acc * i }
}

fun Map<Color, Long>.isDrawPossible(): Boolean {
    val numberOfBallsInGame = mapOf(Color.RED to 12L, Color.GREEN to 13L, Color.BLUE to 14L)
    return when {
        (this[Color.RED] ?: 0) > numberOfBallsInGame[Color.RED]!! -> false
        (this[Color.GREEN] ?: 0) > numberOfBallsInGame[Color.GREEN]!! -> false
        (this[Color.BLUE] ?: 0) > numberOfBallsInGame[Color.BLUE]!! -> false
        else -> return true
    }
}

suspend fun sumOfPossibleGameIds(game: List<String>): Int = coroutineScope {
    game.mapIndexed { index, game -> index + 1 to game }
        .map { async { it.first to it.second.isGamePossible() } }
        .awaitAll()
        .also { println("------") }
        .filter { it.second }.onEach { println(it) }
        .also { println("------") }
        .sumOf { it.first }
}

suspend fun powerOfGames(game: List<String>): Long = coroutineScope {
   game.map { async { it.minPossibleSet().power() } }
            .awaitAll()
            .sumOf { it }
}

data class Balls(val color: Color, val count: Long)

suspend fun main(): Unit = runBlocking(context = Dispatchers.Default) {
    val game = File({}.javaClass.classLoader.getResource("day2.txt")?.file ?: throw FileNotFoundException())
        .readLines()
    val power = async { powerOfGames(game) }
    sumOfPossibleGameIds(game).also(::println)
    power.await().also(::println)
}