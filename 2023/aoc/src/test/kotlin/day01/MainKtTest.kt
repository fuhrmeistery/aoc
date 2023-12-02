package day01

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.io.File
import java.io.FileNotFoundException

class MainKtTest : DescribeSpec({
    describe("should extract all numbers from string") {
        it("should numbers on bounds") {
            "1is2".extractNumber() shouldBe listOf<Long>(1,2)
        }

        it("shold remove all letters") {
            "some1234".extractNumber() shouldBe listOf<Long>(1,2,3,4)
        }
    }

    describe("should calibrate correctly") {
        it("should calibrate single line") {
            "1abc2".lineCalibration() shouldBe 12
        }
    }

    it("should read calibrations from file") {
        val file = File(javaClass.classLoader.getResource("calib-test.txt")?.file ?: throw FileNotFoundException())
        val calibration = readCalibration(file)
        calibration shouldBe 142
    }

    describe("should account for literal numbers") {
        it("some test name") {
            some("two1nine") shouldBe 29
            some("eightwothree") shouldBe 83
            some("abcone2threexyz") shouldBe 13
            some("xtwone3four") shouldBe 24
            some("4nineeightseven2") shouldBe 42
            some("zoneight234") shouldBe 14
            some("7pqrstsixteen") shouldBe 76
        }
    }
})