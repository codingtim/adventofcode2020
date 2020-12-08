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
        val instructions = parseExample(exampleInput)

        val executor = Executor(instructions)
        val result = calculateTillLoop(executor)

        assertEquals(5, result)
    }

    @Test
    internal fun partOne() {
        val instructions = parse(readFile())

        val executor = Executor(instructions)
        val result = calculateTillLoop(executor)

        assertEquals(1528, result)
    }

    //@Test
    internal fun infiniteLoop() {
        val loopInput = """jmp +0
        acc +1
        jmp +4
        acc +3
        jmp -3
        acc -99
        acc +1
        jmp -4
        acc +6"""

        val instructions = parseExample(loopInput)

        val executor = Executor(instructions)
        StopOnExit(executor).execute()
    }

    @Test
    internal fun exampleTwo() {
        val instructions = parseExample(exampleInput)

        val result = swapAndFix(instructions)
        assertEquals(8, result)
    }

    @Test
    internal fun partTwo() {
        val instructions = parse(readFile())

        val result = swapAndFix(instructions)
        assertEquals(640, result)
    }

    private fun calculateTillLoop(executor: Executor): Int {
        try {
            StopOnExit(executor).execute()
        } catch (e: RuntimeException) {
            return executor.accumulator
        }
        return -1
    }

    private fun swapAndFix(instructions: List<Pair<String, Int>>): Int {
        var changeIndex = 0
        while (true) {
            val (nextChangeIndex, instructionsWithReplace) = swapNextOperation(changeIndex, instructions)
            changeIndex = nextChangeIndex
            try {
                return StopOnExit(Executor(instructionsWithReplace)).execute()
            } catch (e: RuntimeException) {

            }
        }
    }

    private fun swapNextOperation(offset: Int, instructions: List<Pair<String, Int>>): Pair<Int, MutableList<Pair<String, Int>>> {
        val instructionsWithReplace = mutableListOf<Pair<String, Int>>()
        instructionsWithReplace.addAll(instructions)
        for (i in offset..instructions.size) {
            if (instructions[i].first == "nop") {
                instructionsWithReplace[i] = "jmp" to instructionsWithReplace[i].second
                return i + 1 to instructionsWithReplace
            }
            if (instructions[i].first == "jmp") {
                instructionsWithReplace[i] = "nop" to instructionsWithReplace[i].second
                return i + 1 to instructionsWithReplace
            }
        }
        throw RuntimeException("No more operations to swap")
    }

    class StopOnExit(private val executor: Executor) {

        private val pointerValuesUsed: MutableList<Int> = mutableListOf()

        fun execute(): Int {
            while (true) {
                pointerValuesUsed.add(executor.pointer)
                executor.processInstruction()
                if (executor.pointer == executor.instructions.size) {
                    return executor.accumulator
                }
                if (pointerValuesUsed.contains(executor.pointer)) throw RuntimeException("Previous visited place detected")
            }
        }
    }

    class Executor(val instructions: List<Pair<String, Int>>) {

        var accumulator: Int = 0
        var pointer: Int = 0
        private var previousPointer: Int = -1

        fun processInstruction() {
            previousPointer = pointer
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
            if (previousPointer == (pointer)) throw RuntimeException("Infinite loop detected")
        }

    }

    private fun parse(instructionStrings: List<String>): List<Pair<String, Int>> {
        return instructionStrings.map { it.split(" ") }.map { it[0] to it[1].toInt() }
    }

    private fun parseExample(example: String): List<Pair<String, Int>> {
        return parse(example.split(System.lineSeparator()).map { it.trim() })
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("08").readText().split(System.lineSeparator()).filter { it.isNotEmpty() }
    }
}