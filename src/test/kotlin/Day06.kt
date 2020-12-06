import org.junit.jupiter.api.Test

class Day06 {

    @Test
    internal fun partOne() {
        val input = readFile()
        var counter = 0;
        val questions = mutableSetOf<Char>()
        for (line in input) {
            if (line.isBlank()) {
                counter += questions.size
                questions.clear()
            } else {
                questions.addAll(line.toHashSet())
            }
        }
        println(counter)
    }

    @Test
    internal fun partTwo() {
        val input = readFile()
        var counter = 0
        var peopleInGroup = 0
        val questions = mutableMapOf<Char, Int>()
        for (line in input) {
            if (line.isBlank()) {
                counter += questions.values.filter { it == peopleInGroup }.count()
                questions.clear()
                peopleInGroup = 0
            } else {
                peopleInGroup++
                for (char in line.toCharArray()) {
                    val count = questions.getOrDefault(char, 0)
                    questions[char] = count + 1
                }
            }
        }
        println(counter)
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("06").readText().split(System.lineSeparator())
    }
}