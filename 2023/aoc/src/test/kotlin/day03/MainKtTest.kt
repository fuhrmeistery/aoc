package day03

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

internal class MainKtTest : DescribeSpec({
    describe("should sum all parts") {
        it("should sum all parts") {
            val engineSchematic = listOf(
                "467..114..",
                "...*......",
                "..35..633.",
                "......#...",
                "617*......",
                ".....+.58.",
                "..592.....",
                "......755.",
                "...$.*....",
                ".664.598..",
            )
            runBlocking { calculateSumOfSchematic(engineSchematic) } shouldBe 4361L
        }
    }
})

internal class RowMapper : FunSpec({
    context("should map rows to schema parts") {
        val rows = listOf<Pair<String, List<SchemaParts>>>(
            "467..114.." to listOf(
                EnginePart(4, 0, 0, 0),
                EnginePart(6, 1, 1, 0),
                EnginePart(7, 2, 2, 0),

                EnginePart(1, 5, 5, 0),
                EnginePart(1, 6, 6, 0),
                EnginePart(4, 7, 7, 0),
            ),

            "...*......" to listOf(Symbol(0, 3)),

            "..35..633." to listOf(
                EnginePart(3, 2, 2, 0),
                EnginePart(5, 3, 3, 0),

                EnginePart(6, 6, 6, 0),
                EnginePart(3, 7, 7, 0),
                EnginePart(3, 8, 8, 0),
            ),


            "......#..." to listOf(Symbol(0, 6)),

            "617*......" to listOf(
                EnginePart(6, 0, 0, 0),
                EnginePart(1, 1, 1, 0),
                EnginePart(7, 2, 2, 0),

                Symbol(0, 3),
            ),

            ".....+.58." to listOf(
                Symbol(0, 5),
                EnginePart(5, 7, 7, 0),
                EnginePart(8, 8, 8, 0),
            ),

            "..592....." to listOf(
                EnginePart(5, 2, 2, 0),
                EnginePart(9, 3, 3, 0),
                EnginePart(2, 4, 4, 0),
            ),

            "......755." to listOf(
                EnginePart(7, 6, 6, 0),
                EnginePart(5, 7, 7, 0),
                EnginePart(5, 8, 8, 0),
            ),

            "...$.*...." to listOf(Symbol(0, 3), Symbol(0, 5)),

            ".664.598.." to listOf(
                EnginePart(6, 1, 1, 0),
                EnginePart(6, 2, 2, 0),
                EnginePart(4, 3, 3, 0),

                EnginePart(5, 5, 5, 0),
                EnginePart(9, 6, 6, 0),
                EnginePart(8, 7, 7, 0),
            ),
        )

        withData(rows) { (row: String, expected: List<SchemaParts>) -> row.mapToSchemaRow(0) shouldBe expected }
    }

    context("should combine numbers correctly") {
        val numbers = listOf(
            listOf(
                EnginePart(4, 0, 0, 0),
                EnginePart(6, 1, 1, 0),
                EnginePart(7, 2, 2, 0),

                EnginePart(1, 5, 5, 0),
                EnginePart(1, 6, 6, 0),
                EnginePart(4, 7, 7, 0),
            ) to listOf(
                EnginePart(467, 0, 2, 0),
                EnginePart(114, 5, 7, 0),
            ),

            listOf(
                EnginePart(6, 1, 1, 0),
                EnginePart(6, 2, 2, 0),
                EnginePart(4, 3, 3, 0),

                EnginePart(5, 5, 5, 0),
                EnginePart(9, 6, 6, 0),
                EnginePart(8, 7, 7, 0),
            ) to listOf(
                EnginePart(664, 1, 3, 0),
                EnginePart(598, 5, 7, 0),
            ),
        )

        withData(numbers) { (parts: List<EnginePart>, expected: List<EnginePart>) ->
            mergeParts(
                parts,
                emptyList()
            ) shouldBe expected
        }
    }

    context("should get engine parts") {

        val schemas = listOf(
            listOf(
                EnginePart(6, 1, 1, 0), Symbol(0, 1)
            )
                    to listOf(EnginePart(6, 1, 1, 0)),

            listOf(
                EnginePart(467, 0, 2, 0),
                EnginePart(114, 5, 7, 0),

                Symbol(0, 3),

                EnginePart(664, 1, 3, 0),
                EnginePart(598, 5, 7, 0),
                Symbol(0, 3),

                )
        )

//        withData(schemas) { (schema, expected) -> getEngineParts(listOf(schema)) shouldBe expected }
    }
})