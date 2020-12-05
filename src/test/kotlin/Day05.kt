import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05 {

    @Test
    internal fun FBFBBFFRLR() {
        val input = "FBFBBFFRLR"
        assertEquals(44, rowValueOf(input))
        assertEquals(5, columnValueOf(input))
        assertEquals(357, seatIdOf(input))
    }

    @Test
    internal fun BFFFBBFRRR() {
        val input = "BFFFBBFRRR"
        assertEquals(70, rowValueOf(input))
        assertEquals(7, columnValueOf(input))
        assertEquals(567, seatIdOf(input))
    }

    @Test
    internal fun FFFBBBFRRR() {
        val input = "FFFBBBFRRR"
        assertEquals(14, rowValueOf(input))
        assertEquals(7, columnValueOf(input))
        assertEquals(119, seatIdOf(input))
    }

    @Test
    internal fun BBFFBBFRLL() {
        val input = "BBFFBBFRLL"
        assertEquals(102, rowValueOf(input))
        assertEquals(4, columnValueOf(input))
        assertEquals(820, seatIdOf(input))
    }

    @Test
    internal fun partOne() {
        val maxBy = readFile()
                .map { it to seatIdOf(it) }
                .maxByOrNull { it.second }
        println(maxBy!!)
    }

    @Test
    internal fun partTwo() {
        val missing = readFile()
                .map { it to seatIdOf(it) }
                .sortedBy { it.second }
                .reduce { acc, pair ->
                    if (acc.second == pair.second - 1) pair else acc
                }
        println(missing.second + 1)
    }

    private fun seatIdOf(input: String): Int =
            rowValueOf(input) * 8 + columnValueOf(input)

    private fun rowValueOf(input: String) =
            valueOf('F', 'B', 0, 127, input.substring(0, 7))

    private fun columnValueOf(input: String) =
            valueOf('L', 'R', 0, 7, input.substring(7, 10))

    private fun valueOf(lowerChar: Char, upperChar: Char, lowerBound: Int, upperBound: Int, input: String): Int {

        fun withIndex(index: Int, lower: Int, upper: Int): Int {
            if (index == input.length) return lower;
            val c = input[index]
            val remaining = upper - lower
            return if (c == lowerChar) {
                withIndex(index + 1, lower, upper - 1 - remaining / 2)
            } else {
                withIndex(index + 1, lower + 1 + remaining / 2, upper)
            }
        }

        return withIndex(0, lowerBound, upperBound)
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("05").readText().split(System.lineSeparator()).filter { it.isNotBlank() }
    }
}