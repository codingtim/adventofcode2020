import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day08 {

    private val exampleInput = """nop +0
            acc +1
            jmp +4
            acc +3
            jmp -3
            acc -99
            acc +1
            jmp -4
            acc +6"""

    @Test
    internal fun exampleOne() {
        val instructions = parse(exampleInput.split(System.lineSeparator()).map { it.trim() })

        val executor = Executor(instructions)
        executor.execute()

        assertEquals(5, executor.accumulator)
    }

    @Test
    internal fun partOne() {
        val instructions = parse(readFile())

        val executor = Executor(instructions)
        executor.execute()

        println(executor.accumulator)
    }

    class Executor(private val instructions: List<Pair<String, Int>>) {

        var accumulator = 0;
        private var pointer = 0;

        private var keepRunning = true;
        private var pointerValuesUsed = mutableListOf<Int>()

        fun execute() {
            while (keepRunning) {
                pointerValuesUsed.add(pointer)
                val (operation, argument) = instructions[pointer]
                when (operation) {
                    "acc" -> {
                        accumulator += argument
                        pointer++
                    }
                    "nop" -> {
                        pointer++
                    }
                    "jmp" -> {
                        pointer += argument
                    }
                    else -> throw RuntimeException("Unknown operation $operation argument $argument")
                }
                if (pointerValuesUsed.contains(pointer)) keepRunning = false
            }
        }

    }

    private fun parse(instructionStrings: List<String>): List<Pair<String, Int>> {
        return instructionStrings.map { it.split(" ") }.map { it[0] to it[1].toInt() }
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("08").readText().split(System.lineSeparator()).filter { it.isNotEmpty() }
    }
}