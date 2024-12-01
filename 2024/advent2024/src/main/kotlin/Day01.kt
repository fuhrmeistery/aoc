fun main() {
    val lists = Utils.readLinesFrom("./day01.txt")!!
        .asSequence()
        .map { it.split(" ") }
        .map { Pair(it.first(), it.last()) }
        .unzip()

    val first = lists.first.sorted()
    val second = lists.second.sorted()

    println(first.mapIndexed { index, s -> Math.abs(s.toInt() - second[index].toInt()) }.sum())
    println(first.sumOf { s -> s.toInt() * second.count { it == s } })
}