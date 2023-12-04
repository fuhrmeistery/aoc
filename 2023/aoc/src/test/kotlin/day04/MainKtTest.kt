package day04

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldContainAnyOf
import io.kotest.matchers.shouldBe

internal class Scratcher : FunSpec({

    context("should split into winning and actual numbers") {
        val scratchers = listOf(
            "41 48 83 86 17 | 83 86  6 31 17  9 48 53" to Pair("41 48 83 86 17", "83 86  6 31 17  9 48 53"),
            "13 32 20 16 61 | 61 30 68 82 17 32 24 19" to Pair("13 32 20 16 61", "61 30 68 82 17 32 24 19"),
            "41 92 73 84 69 | 59 84 76 51 58  5 54 83" to Pair("41 92 73 84 69", "59 84 76 51 58  5 54 83"),
            "87 83 26 28 32 | 88 30 70 12 93 22 82 36" to Pair("87 83 26 28 32", "88 30 70 12 93 22 82 36"),
            "31 18 13 56 72 | 74 77 10 23 35 67 36 11" to Pair("31 18 13 56 72", "74 77 10 23 35 67 36 11"),
        )

        withData(scratchers) { (scratcher, expected) -> scratcher.splitByPipe() shouldBe expected }
    }

    context("should convert String to number list") {
        val cards = listOf(
            "41 48 83 86 17" to listOf(41, 48, 83, 86, 17),
            "13 32 20 16 61" to listOf(13, 32, 20, 16, 61),
            "41 92 73 84 69" to listOf(41, 92, 73, 84, 69),
            "87 83 26 28 32" to listOf(87, 83, 26, 28, 32),
            "31 18 13 56 72" to listOf(31, 18, 13, 56, 72),
        )

        withData(cards) { (numberString, numberList) -> numberString.splitNumbers() shouldBe numberList }
    }

    context("should find winning numbers") {
        val scratchers = listOf<Pair<String, List<Long>>>(
            "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53" to listOf(48, 83, 17, 86),
            "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19" to listOf(32, 61),
            "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1" to listOf(1, 21),
            "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83" to listOf(84),
//            "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36" to emptyList(),
//            "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11" to emptyList(),
        )

        withData(scratchers) { (scratcher, winningNumbers) -> runBlocking { getWinningNumbers(scratcher) } shouldContainAnyOf winningNumbers }
    }

    context("should count wining score") {
        val wins = listOf<Pair<List<Long>, Long>>(
            listOf<Long>(48, 83, 17, 86) to 8L,
            listOf<Long>(32, 61) to 2L,
            listOf<Long>(1, 21) to 2L,
            listOf<Long>(84) to 2L,
        )

        withData(wins) { (winningNumbers, score) -> getScore(winningNumbers, score) }
    }

    context("should count scratchers received") {
        val initialScratchers = listOf(
            listOf(
                "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53",
                "Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19",
                "Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1",
                "Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83",
                "Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36",
                "Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11",
            ) to 30L
        )

        withData(initialScratchers) { (scratchers, total) -> runBlocking { scratchers(scratchers, scratchers) } shouldBe total }
    }
})