import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day02 {

    @Test
    internal fun parse() {
        val (policy, password) = parsePasswordPolicy("1-3 a: abcde") { lowerBound: Int, upperBound: Int, letter: Char -> SledRentalPasswordPolicy(lowerBound, upperBound, letter) }
        assertEquals(policy, SledRentalPasswordPolicy(1, 3, 'a'))
        assertEquals(password, "abcde")
    }

    @Test
    internal fun sledRentalPasswordPolicy() {
        assertTrue(SledRentalPasswordPolicy(1, 3, 'a').matches("abcde"))
        assertFalse(SledRentalPasswordPolicy(1, 3, 'b').matches("cdefg"))
        assertTrue(SledRentalPasswordPolicy(2, 9, 'c').matches("ccccccccc"))
    }

    @Test
    internal fun partOne() {
        val numberOfValidPasswords = countValidPasswords { lowerBound: Int, upperBound: Int, letter: Char -> SledRentalPasswordPolicy(lowerBound, upperBound, letter) }
        println(numberOfValidPasswords)
    }

    @Test
    internal fun tobogganCorporatePasswordPolicy() {
        assertTrue(TobogganCorporatePasswordPolicy(1, 3, 'a').matches("abcde"))
        assertFalse(TobogganCorporatePasswordPolicy(1, 3, 'b').matches("cdefg"))
        assertFalse(TobogganCorporatePasswordPolicy(2, 9, 'c').matches("ccccccccc"))
    }

    @Test
    internal fun partTwo() {
        val numberOfValidPasswords = countValidPasswords { position1: Int, position2: Int, letter: Char -> TobogganCorporatePasswordPolicy(position1, position2, letter) }
        println(numberOfValidPasswords)
    }

    private fun countValidPasswords(passwordPolicy: (Int, Int, Char) -> PasswordPolicy): Int {
        return readFile().map { inputString -> parsePasswordPolicy(inputString, passwordPolicy) }
                .map { (policy, password) -> policy.matches(password) }
                .map { passwordMatchesPolicy ->  if(passwordMatchesPolicy) 1 else 0 }
                .sum()
    }

    interface PasswordPolicy {
        fun matches(password: String): Boolean
    }

    data class SledRentalPasswordPolicy(val lowerBound: Int, val upperBound: Int, val letter: Char) : PasswordPolicy {
        override fun matches(password: String): Boolean {
            val occurances = password.toCharArray()
                    .filter { it == letter }
                    .count()
            return occurances in lowerBound..upperBound
        }
    }

    data class TobogganCorporatePasswordPolicy(val position1: Int, val position2: Int, val letter: Char) : PasswordPolicy {
        override fun matches(password: String): Boolean {
            val oneMatches = password[position1 - 1] == letter || password[position2 - 1] == letter
            val bothMatch = password[position1 - 1] == letter && password[position2 - 1] == letter
            return oneMatches && !bothMatch
        }
    }

    private fun parsePasswordPolicy(stringToParse: String, passwordPolicy: (Int, Int, Char) -> PasswordPolicy): Pair<PasswordPolicy, String> {
        val parts = stringToParse.split(" ")
        val bounds = parts[0].split("-")
        val lowerBound = bounds[0].toInt()
        val upperBound = bounds[1].toInt()
        val letter = parts[1].replace(":", "")[0]
        val password = parts[2]
        return passwordPolicy.invoke(lowerBound, upperBound, letter) to password
    }

    private fun readFile(): List<String> {
        return javaClass.getResource("02").readText().split(System.lineSeparator()).filter { it.isNotBlank() }
    }
}