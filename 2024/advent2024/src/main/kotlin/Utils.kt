object Utils {
    fun readLinesFrom(filename: String) =
        this::class.java.getResourceAsStream(filename)
            ?.bufferedReader()
            ?.readLines()
}
