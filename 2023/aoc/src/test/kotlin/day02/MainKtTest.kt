package day02

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

typealias GamePair = Pair<List<Balls>, Map<Color, Long>>

internal class MainKtTest : DescribeSpec({

    describe("should find all possible games") {
        it("should split by colon") {
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red".splitByColon().first() shouldBe "Game 3"
        }

        it("should get of first game") {
            "Game 1".getId() shouldBe 1
        }

        it("should separate games") {
            val draws = "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green".drawsAsList()
            draws.first() shouldBe "3 blue, 4 red"
            draws[1] shouldBe "1 red, 2 green, 6 blue"
            draws.last() shouldBe "2 green"
        }

        it("should count balls for color") {
            "1 red".mapToBalls() shouldBe Balls(Color.RED, 1)
        }

        it("should find max amount per color") {
            val balls = listOf(
                Balls(Color.GREEN, 8),
                Balls(Color.GREEN, 13),
                Balls(Color.GREEN, 5),

                Balls(Color.BLUE, 6),
                Balls(Color.BLUE, 5),

                Balls(Color.RED, 20),
                Balls(Color.RED, 4),
                Balls(Color.RED, 1),
            )

            balls.findMaxBallsPerColor() shouldBe mapOf(Color.RED to 20, Color.GREEN to 13, Color.BLUE to 6)
        }
    }

    describe("should read file and find all possible games") {
        it("should find all possible games") {
            val games = listOf(
                "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green",
                "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue",
                "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red",
                "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red",
                "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"
            )
           runBlocking { sumOfPossibleGameIds(games) } shouldBe 8
        }
    }

    describe("should calc power correctrly") {
        it("should find minimal Set correctly") {
            "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green".minPossibleSet() shouldBe mapOf<Color, Long>(Color.RED to 4,  Color.GREEN to 2, Color.BLUE to 6)
        }

        it("should calc power correctly") {
            mapOf<Color, Long>(Color.RED to 4,  Color.GREEN to 2, Color.BLUE to 6).power() shouldBe 48
        }
    }
})

internal class GameTest : FunSpec({
    context("should find all possible games") {
        withData(
            Pair("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green", true),
            Pair("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue", true),
            Pair("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red", false),
            Pair("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red", false),
            Pair("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green", true),
        ) { (game, expected) -> game.isGamePossible() shouldBe expected }
    }

    context("should find max color count") {
        val games: List<GamePair> = listOf(
            Pair(
                listOf(
                    Balls(Color.GREEN, 8),
                    Balls(Color.GREEN, 13),
                    Balls(Color.GREEN, 5),

                    Balls(Color.BLUE, 6),
                    Balls(Color.BLUE, 5),

                    Balls(Color.RED, 20),
                    Balls(Color.RED, 4),
                    Balls(Color.RED, 1),
                ),
                mapOf(Color.RED to 20, Color.GREEN to 13, Color.BLUE to 6)
            ),
            Pair(
                listOf(
                    Balls(Color.BLUE, 1),
                    Balls(Color.BLUE, 6),

                    Balls(Color.RED, 4),
                    Balls(Color.RED, 1),

                    Balls(Color.GREEN, 2),
                    Balls(Color.GREEN, 2),
                ),
                mapOf(Color.BLUE to 6, Color.RED to 4, Color.GREEN to 2)
            ),
            Pair(
                listOf(
                    Balls(Color.BLUE, 1),
                    Balls(Color.BLUE, 4),
                    Balls(Color.BLUE, 1),

                    Balls(Color.GREEN, 2),
                    Balls(Color.GREEN, 3),
                    Balls(Color.GREEN, 1),

                    Balls(Color.RED, 1),
                ), mapOf(Color.BLUE to 4, Color.GREEN to 3, Color.RED to 1)
            ),
            Pair(
                listOf(
                    Balls(Color.GREEN, 1),
                    Balls(Color.GREEN, 3),

                    Balls(Color.RED, 3),
                    Balls(Color.RED, 6),
                    Balls(Color.RED, 14),

                    Balls(Color.BLUE, 6),
                    Balls(Color.BLUE, 15),
                ), mapOf(Color.GREEN to 3, Color.RED to 14, Color.BLUE to 15)
            ),
            Pair(
                listOf(
                    Balls(Color.RED, 6),
                    Balls(Color.RED, 1),

                    Balls(Color.BLUE, 1),
                    Balls(Color.BLUE, 2),

                    Balls(Color.GREEN, 3),
                    Balls(Color.GREEN, 2),
                ), mapOf(Color.RED to 6, Color.BLUE to 2, Color.GREEN to 3)
            )
        )

        withData(games) { (balls, expected) -> balls.findMaxBallsPerColor() shouldBe expected }
    }

    context("should correctly check if game is possible") {
        // only 12 red cubes, 13 green cubes, and 14 blue cubes
        val games =
            listOf(
                mapOf<Color, Long>(Color.RED to 20, Color.GREEN to 13, Color.BLUE to 6) to false,
                mapOf<Color, Long>(Color.RED to 4, Color.GREEN to 2, Color.BLUE to 6) to true,
                mapOf<Color, Long>(Color.RED to 1, Color.GREEN to 3, Color.BLUE to 4) to true,
                mapOf<Color, Long>(Color.RED to 14, Color.GREEN to 3, Color.BLUE to 15) to false,
                mapOf<Color, Long>(Color.RED to 6, Color.GREEN to 3, Color.BLUE to 2) to true,
            )
        withData(games) { (game, expected) -> game.isDrawPossible() shouldBe expected }
    }
})
