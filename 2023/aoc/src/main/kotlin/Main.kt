import java.io.File
import java.io.FileNotFoundException

fun String.extractNumber(): List<Long> =
    this.filter { it.isDigit() }
        .map { it.digitToInt().toLong() }

fun String.lineCalibration(): Long =
    this.extractNumber().first() * 10 + this.extractNumber().last()

fun File.readCalibration(): Long =
    this.readLines()
        .map(String::mapSpelledOutNumbers)
        .map(String::lineCalibration)
        .sum()

fun String.mapSpelledOutNumbers(): String =
    this.replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")
        .replace("zero", "0")

fun main() {
    val file = File({}.javaClass.classLoader.getResource("calib.txt")?.file ?: throw FileNotFoundException())
    val cal = file.readCalibration()
    println(cal)
}