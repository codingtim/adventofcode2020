import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03 {

    @Test
    internal fun example() {
        val input = """
            ..##.......
            #...#...#..
            .#....#..#.
            ..#.#...#.#
            .#...##..#.
            ..#.##.....
            .#.#.#....#
            .#........#
            #.##...#...
            #...##....#
            .#..#...#.#
        """
        val inputLines = input.split(System.lineSeparator()).map { it.trim() }.filter { it.isNotEmpty() }
        val deltaX = 3
        val deltaY = 1
        val biomeRepetitionSize = inputLines[0].length
        val location = 0 to 0

        val treeCounter = navigateThrough(inputLines, location, deltaY to deltaX, biomeRepetitionSize)

        assertEquals(7, treeCounter)
    }

    @Test
    internal fun partOne() {
        val input = readFile()
        val deltaX = 3
        val deltaY = 1
        val biomeRepetitionSize = input[0].length

        val treeCounter = navigateThrough(input, 0 to 0, deltaY to deltaX, biomeRepetitionSize)
        println(treeCounter)
    }

    @Test
    internal fun partTwo() {
        val input = readFile()

        val deltasToCount = listOf(
                1 to 1,
                1 to 3,
                1 to 5,
                1 to 7,
                2 to 1
        )

        val biomeRepetitionSize = input[0].length

        val treeCounter = deltasToCount
                .map { delta -> navigateThrough(input, 0 to 0, delta, biomeRepetitionSize) }
                .map { it.toLong() }
                .foldRight(1, { a: Long, b: Long -> a * b })
        println(treeCounter)
    }

    private fun navigateThrough(map: List<String>, startPosition: Pair<Int, Int>, delta: Pair<Int, Int>, biomeRepetitionSize: Int): Int {
        val navigateTo = map.size

        tailrec fun arrivedAt(currentPosition: Pair<Int, Int>, treesEncountered: Int): Int {
            if (currentPosition.first >= navigateTo) return treesEncountered
            val snowOrTreeAtLocation = map[currentPosition.first][currentPosition.second % biomeRepetitionSize]
            return arrivedAt(
                    currentPosition.first + delta.first to currentPosition.second + delta.second,
                    treesEncountered + (if (snowOrTreeAtLocation == '#') 1 else 0)
            )
        }
        return arrivedAt(startPosition, 0)
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("03").readText().split(System.lineSeparator()).filter { it.isNotBlank() }
    }
}