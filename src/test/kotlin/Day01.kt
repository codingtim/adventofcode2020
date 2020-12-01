import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day01 {

    @Test
    internal fun sumTwoNumbersTo2020() {
        val input = setOf(1721, 979, 366, 299, 675, 1456)
        val first = solvePair(input)
        assertEquals(514579, first)
    }

    @Test
    internal fun partOne() {
        val input = readFile().map { it.toInt() }.toSet()
        val first = solvePair(input)
        println(first)
    }

    private fun solvePair(input: Set<Int>): Int =
            lazyPairCombinations(input)
                    .filter { pair -> pair.first + pair.second == 2020 }
                    .map { pair -> pair.first * pair.second }
                    .first()


    private fun lazyPairCombinations(setOfNumbers: Set<Int>): Sequence<Pair<Int, Int>> =
            sequence {
                for (number in setOfNumbers) {
                    val tail = setOfNumbers.minus(number)
                    combinationsOf(number, tail)
                }
            }

    private suspend fun SequenceScope<Pair<Int, Int>>.combinationsOf(head: Int, tail: Set<Int>) =
            tail.forEach { tailValue ->
                yield(head to tailValue)
            }

    @Test
    internal fun sumThreeNumbersTo2020() {
        val input = setOf(1721, 979, 366, 299, 675, 1456)
        val first = solveTriple(input)
        assertEquals(241861950, first)
    }

    @Test
    internal fun partTwo() {
        val input = readFile().map { it.toInt() }.toSet()
        val first = solveTriple(input)
        println(first)
    }

    private fun solveTriple(input: Set<Int>): Int =
            lazyTripleCombinations(input)
                    .filter { triple -> triple.first + triple.second + triple.third == 2020 }
                    .map { triple -> triple.first * triple.second * triple.third }
                    .first()


    private fun lazyTripleCombinations(setOfNumbers: Set<Int>): Sequence<Triple<Int, Int, Int>> =
            sequence {
                for (first in setOfNumbers) {
                    val tailFirst = setOfNumbers.minus(first)
                    for (second in tailFirst) {
                        val tailSecond = tailFirst.minus(second)
                        combinationsOf(first, second, tailSecond)
                    }
                }
            }

    private suspend fun SequenceScope<Triple<Int, Int, Int>>.combinationsOf(first: Int, second: Int, tail: Set<Int>) =
            tail.forEach { tailValue ->
                yield(Triple(first, second, tailValue))
            }


    private fun readFile(): List<String> {
        return javaClass.getResource("01").readText().split(System.lineSeparator()).filter { it.isNotBlank() }
    }


}