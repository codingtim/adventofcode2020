import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04 {

    private val requiredFields = setOf(
            "byr",
            "iyr",
            "eyr",
            "hgt",
            "hcl",
            "ecl",
            "pid",
    )

    @Test
    internal fun examplePartOne() {
        val input = """ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
            byr:1937 iyr:2017 cid:147 hgt:183cm

            iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
            hcl:#cfa07d byr:1929

            hcl:#ae17e1 iyr:2013
            eyr:2024
            ecl:brn pid:760753108 byr:1931
            hgt:179cm

            hcl:#cfa07d eyr:2025 pid:166559648
            iyr:2011 ecl:brn hgt:59in
            """
        val inputLines = input.split(System.lineSeparator()).map { it.trim() }
        val counter = countValidPassports(inputLines, ::hasAllRequiredFields)
        assertEquals(2, counter)
    }

    @Test
    internal fun partOne() {
        val countValidPassports = countValidPassports(readFile(), ::hasAllRequiredFields)
        assertEquals(204, countValidPassports)
    }

    @Test
    internal fun examplePartTwo_noValid() {
        val input = """eyr:1972 cid:100
            hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926
            
            iyr:2019
            hcl:#602927 eyr:1967 hgt:170cm
            ecl:grn pid:012533040 byr:1946
            
            hcl:dab227 iyr:2012
            ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277
            
            hgt:59cm ecl:zzz
            eyr:2038 hcl:74454a iyr:2023
            pid:3556412378 byr:2007
            """
        val inputLines = input.split(System.lineSeparator()).map { it.trim() }
        val counter = countValidPassports(inputLines, both(::hasAllRequiredFields, ::requiredFieldsHaveCorrectValues))
        assertEquals(0, counter)
    }

    @Test
    internal fun examplePartTwo_allValid() {
        val input = """pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980
            hcl:#623a2f
            
            eyr:2029 ecl:blu cid:129 byr:1989
            iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm
            
            hcl:#888785
            hgt:164cm byr:2001 iyr:2015 cid:88
            pid:545766238 ecl:hzl
            eyr:2022
            
            iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719
            """
        val inputLines = input.split(System.lineSeparator()).map { it.trim() }
        val counter = countValidPassports(inputLines, both(::hasAllRequiredFields, ::requiredFieldsHaveCorrectValues))
        assertEquals(4, counter)
    }

    @Test
    internal fun partTwo() {
        val countValidPassports = countValidPassports(readFile(), both(::hasAllRequiredFields, ::requiredFieldsHaveCorrectValues))
        println(countValidPassports)
    }

    private fun both(f1: (Passport) -> Boolean, f2: (Passport) -> Boolean): (Passport) -> Boolean {
        return { passport -> f1.invoke(passport) && f2.invoke(passport) }
    }

    private fun countValidPassports(inputLines: List<String>, validFunction: (Passport) -> Boolean): Int {
        return parsePassports(inputLines)
                .filter { passport -> validFunction.invoke(passport) }
                .count()
    }

    private fun hasAllRequiredFields(passport: Passport): Boolean =
            passport.fields.map { it.key }.containsAll(requiredFields)

    private fun requiredFieldsHaveCorrectValues(passport: Passport): Boolean =
            passport.fields.all { fieldHasCorrectValues(it) }

    private fun fieldHasCorrectValues(passportField: PassportField): Boolean =
            when (passportField.key) {
                "byr" -> isCorrectYear(passportField.value, 1920, 2002)
                "iyr" -> isCorrectYear(passportField.value, 2010, 2020)
                "eyr" -> isCorrectYear(passportField.value, 2020, 2030)
                "hgt" -> isCorrectHeight(passportField.value)
                "hcl" -> isCorrectHairColor(passportField.value)
                "ecl" -> isCorrectEyeColor(passportField.value)
                "pid" -> isCorrectPassportId(passportField.value)
                else -> true
            }

    private fun isCorrectPassportId(value: String): Boolean =
            value.matches(Regex("0*[0-9]{9}"))

    private fun isCorrectEyeColor(value: String): Boolean =
            value.matches(Regex("amb|blu|brn|gry|grn|hzl|oth"))

    private fun isCorrectHairColor(value: String): Boolean =
            value.matches(Regex("#[0-9a-f]{6}"))


    private fun isCorrectHeight(value: String): Boolean =
            (value.matches(Regex("[0-9][0-9][0-9]cm")) && value.replace("cm", "").toInt() in 150..193) ||
                    (value.matches(Regex("[0-9][0-9]in")) && value.replace("in", "").toInt() in 59..76)


    private fun isCorrectYear(value: String, lowerBound: Int, upperBound: Int): Boolean =
            value.matches(Regex("[0-9][0-9][0-9][0-9]")) && value.toInt() in lowerBound..upperBound


    private fun parsePassports(inputLines: List<String>): List<Passport> {
        val passports = mutableListOf<Passport>()
        var passportFields = mutableSetOf<PassportField>()
        for (line in inputLines) {
            if (line.isEmpty()) {
                passports.add(Passport(passportFields))
                passportFields = mutableSetOf()
            } else {
                passportFields.addAll(line.split(" ").map { it.split(":") }.map { PassportField(it[0], it[1]) })
            }
        }
        return passports
    }

    data class Passport(val fields: Set<PassportField>)

    data class PassportField(val key: String, val value: String)

    private fun readFile(): List<String> {
        return javaClass.getResource("04").readText().split(System.lineSeparator())
    }
}