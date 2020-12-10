import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09 {

    private val exampleInput = """35
                20
                15
                25
                47
                40
                62
                55
                65
                95
                102
                117
                150
                182
                127
                219
                299
                277
                309
                576""".split(System.lineSeparator()).map { it.trim() }.map { it.toLong() }

    @Test
    internal fun exampleOne() {
        val input = exampleInput
        val validated = validate(input, 5)
        assertEquals(
                listOf(true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true, true, true, true),
                validated
        )
    }

    @Test
    internal fun exampleOneAsNumber() {
        val firstFailingNumber = getFirstFailingNumber(exampleInput, 5)
        assertEquals(127, firstFailingNumber)
    }

    @Test
    internal fun partOne() {
        val firstFailingNumber = getFirstFailingNumber(readFile().map { it.toLong() }, 25)
        println(firstFailingNumber)
    }

    @Test
    internal fun exampleTwo() {
        val input = exampleInput
        val toFind: Long = 127

        val sumOfRangeThatMatches = findRangeThatMatches(input, toFind)
        assertEquals(62, sumOfRangeThatMatches)
    }

    @Test
    internal fun partTwo() {
        val toFind: Long = 14144619
        val sumOfRangeThatMatches = findRangeThatMatches(readFile().map { it.toLong() }, toFind)
        println(sumOfRangeThatMatches)
    }

    private fun findRangeThatMatches(input: List<Long>, toFind: Long): Long {
        for (i in 0..input.size) {
            var counter: Long = input[i]
            for (j in i+1..input.size) {
                counter += input[j]
                if (counter == toFind) {
                    val numbersThatSum = input.subList(i, j).sorted()
                    return numbersThatSum.first() + numbersThatSum.last()
                }
                if (counter > toFind) {
                    break
                }
            }
        }
        return -1
    }


    private fun getFirstFailingNumber(input: List<Long>, preambleSize: Int): Long {
        val indexOfFirstFailing = validate(input, preambleSize).mapIndexed { index, value -> index to value }.filter { it.second == false }.first().first
        return input[indexOfFirstFailing]
    }

    private fun validate(input: List<Long>, preambleSize: Int): List<Boolean> {

        fun containsSum(index: Int, value: Long): Boolean {
            val fromIndex = index - preambleSize
            val toIndex = index - 1
            for (i in fromIndex..toIndex) {
                for (j in fromIndex..toIndex) {
                    if (i != j) {
                        if (input[i] + input[j] == value && input[i] != input[j]) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun isValid(index: Int, value: Long): Boolean {
            return if (index < preambleSize) true else containsSum(index, value)
        }

        return input.mapIndexed { index, value -> isValid(index, value) }
    }


    private fun readFile(): List<String> {
        return javaClass.getResource("09").readText().split(System.lineSeparator()).filter { it.isNotEmpty() }
    }
}