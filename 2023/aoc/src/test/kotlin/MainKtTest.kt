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
        val calibration = file.readCalibration()
        calibration shouldBe 142
    }

    describe("should replace spelled out numbers with digits") {
        it("should map 'one' to '1'") {
            "one".mapSpelledOutNumbers() shouldBe "1"
        }
    }
})