package day03

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


suspend fun calculateSumOfSchematic(schemaPlan: List<String>): Long = coroutineScope {
    val schema = schemaPlan.mapIndexed { index, schemaRow -> async { schemaRow.mapToSchemaRow(index) } }
        .awaitAll()
    val engineParts = getEngineParts(schema)
    return@coroutineScope engineParts.sumOf { it.number }
}

sealed class SchemaParts

data class EnginePart(
    val number: Long,
    val startIndex: Int,
    val endIndex: Int,
    val rowIndex: Int,
    val isEnginePart: Boolean = false
) : SchemaParts() {
    fun combineWith(other: EnginePart): EnginePart =
        this.copy(number = this.number.combineWith(other.number), endIndex = other.endIndex)
}

fun Long.combineWith(other: Long): Long = (this.toString() + other.toString()).toLong()

data class Symbol(val rowIndex: Int, val columIndex: Int) : SchemaParts()

fun mergeParts(parts: List<SchemaParts>, mergedList: List<SchemaParts>): List<SchemaParts> {
    if (parts.isEmpty()) return mergedList
    if (mergedList.isEmpty()) return mergeParts(parts.drop(1), listOf(parts.first()))

    return when (val current = parts.first()) {
        is Symbol -> mergeParts(parts.drop(1), mergedList + current)
        is EnginePart -> mergeEngineParts(parts.drop(1), mergedList, current)
    }
}

internal fun List<SchemaParts>.findEngineParts(): Boolean = this.any { it is Symbol }

internal fun getEngineParts(schema: List<List<SchemaParts>>): List<EnginePart> =
    schema.flatten()
        .filterIsInstance<EnginePart>()
        .filter { enginePart ->
            schema.flatten().filterIsInstance<Symbol>()
                .any { (it.columIndex in enginePart.adjacentColumns()).and(it.rowIndex in enginePart.adjacentRows()) }
        }

internal fun EnginePart.adjacentColumns() = (startIndex - 1)..(endIndex + 1)

internal fun EnginePart.adjacentRows() = (rowIndex - 1)..(rowIndex + 1)

internal fun mergeEngineParts(
    parts: List<SchemaParts>,
    mergedList: List<SchemaParts>,
    current: EnginePart
): List<SchemaParts> = when (mergedList.last()) {
    is Symbol -> mergeParts(parts, mergedList + current)
    is EnginePart -> mergeIfPossible(parts, mergedList, current)
}

internal fun mergeIfPossible(
    remainingParts: List<SchemaParts>,
    mergedList: List<SchemaParts>,
    current: EnginePart
): List<SchemaParts> {
    val last = mergedList.last()
    check(last is EnginePart) { "Last item is not an engine part" }
    val newMerge =
        if (last.endIndex + 1 == current.startIndex) mergedList.mergeWithLastEnginePart(current) else mergedList + current
    return mergeParts(remainingParts, newMerge)
}

internal fun List<SchemaParts>.mergeWithLastEnginePart(item: EnginePart): List<SchemaParts> {
    require(this.isNotEmpty()) { "This list does not have any items" }
    val lastAdded = this.last()
    check(lastAdded is EnginePart) { "Last item is not an engine part" }
    val combined = lastAdded.combineWith(item)
    return this.dropLast(1) + combined
}

internal fun String.mapToSchemaRow(rowIndex: Int): List<SchemaParts> {
    return this.foldIndexed(emptyList()) { index, acc, c ->
        when {
            c.isDigit() -> acc + EnginePart(
                number = c.digitToInt().toLong(),
                startIndex = index,
                endIndex = index,
                rowIndex = rowIndex
            )

            c == '.' -> acc

            else -> acc + Symbol(
                rowIndex = rowIndex,
                columIndex = index,
            )
        }
    }
}