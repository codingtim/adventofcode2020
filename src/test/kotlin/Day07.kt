import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant

class Day07 {

    private val exampleInput = """light red bags contain 1 bright white bag, 2 muted yellow bags.
            dark orange bags contain 3 bright white bags, 4 muted yellow bags.
            bright white bags contain 1 shiny gold bag.
            muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
            shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
            dark olive bags contain 3 faded blue bags, 4 dotted black bags.
            vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
            faded blue bags contain no other bags.
            dotted black bags contain no other bags.""".trimIndent().split(System.lineSeparator()).map { it.trim() }

    @Test
    internal fun parse() {
        val bagTypes = parse(exampleInput)
        assertEquals(listOf(
                Bag("light red", mapOf("bright white" to 1, "muted yellow" to 2)),
                Bag("dark orange", mapOf("bright white" to 3, "muted yellow" to 4)),
                Bag("bright white", mapOf("shiny gold" to 1)),
                Bag("muted yellow", mapOf("shiny gold" to 2, "faded blue" to 9)),
                Bag("shiny gold", mapOf("dark olive" to 1, "vibrant plum" to 2)),
                Bag("dark olive", mapOf("faded blue" to 3, "dotted black" to 4)),
                Bag("vibrant plum", mapOf("faded blue" to 5, "dotted black" to 6)),
                Bag("faded blue", mapOf()),
                Bag("dotted black", mapOf())
        ), bagTypes)
    }

    @Test
    internal fun goldenBags() {
        val bagTypes = parse(exampleInput)
        assertEquals(4, countCanContainGold(bagTypes))
    }

    private fun countCanContainGold(bagTypes: List<Bag>): Int {
        // naive solution, input given in correct order
        val canContain = mutableMapOf<String, Boolean>()
        for (bagType in bagTypes.reversed()) {
            canContain[bagType.color] = bagType.bags.containsKey("shiny gold") || bagType.bags.keys.any { canContain[it] == true }
        }
        println(canContain)
        return canContain.filter { it.value }.count()
    }

    @RepeatedTest(10)
    internal fun goldenBagsRandom() {
        val bagTypes = parse(exampleInput)
        assertEquals(4, countCanContainGoldWithoutOrder(bagTypes.shuffled()))
    }

    private fun countCanContainGoldWithoutOrder(bagTypes: List<Bag>): Int {
        val canBagHoldShinyGold = CanBagHoldShinyGold(bagTypes)
        return bagTypes.map { canBagHoldShinyGold.canContain(it) }.filter { it }.count()
    }

    @Test
    internal fun partOne() {
        val bagTypes = parse(readFile())
        println(countCanContainGoldWithoutOrder(bagTypes))
    }

    private class CanBagHoldShinyGold(bagTypes: List<Bag>) {

        private var bagTypesAsMap: Map<String, Bag>
        private val canContain = mutableMapOf<Bag, Boolean>()

        init {
            bagTypesAsMap = bagTypes.map { bagType -> bagType.color to bagType }.toMap()
        }

        fun canContain(bag: Bag): Boolean {
            val canBagContainGoldenShiny = canContain[bag]
            return if (canBagContainGoldenShiny != null) {
                canBagContainGoldenShiny
            } else {
                val computedCanBagContainGoldenShiny = if (bag.bags.isEmpty()) {
                    false
                } else {
                    bag.bags.containsKey("shiny gold") || bag.bags.map { bagTypesAsMap[it.key] }.any { canContain(it!!) }
                }
                canContain[bag] = computedCanBagContainGoldenShiny
                computedCanBagContainGoldenShiny
            }
        }

    }

    @Test
    internal fun goldenBagsNumberOfBagsRequired() {
        val bagTypes = parse(exampleInput)
        assertEquals(32, NumberOfBagsRequired(bagTypes).numberOfBagsRequired("shiny gold"))
    }

    @Test
    internal fun goldenBagsNumberOfBagsRequired_exampleTwo() {
        val input = """shiny gold bags contain 2 dark red bags.
            dark red bags contain 2 dark orange bags.
            dark orange bags contain 2 dark yellow bags.
            dark yellow bags contain 2 dark green bags.
            dark green bags contain 2 dark blue bags.
            dark blue bags contain 2 dark violet bags.
            dark violet bags contain no other bags.""".trimIndent().split(System.lineSeparator()).map { it.trim() }
        val bagTypes = parse(input)
        assertEquals(126, NumberOfBagsRequired(bagTypes).numberOfBagsRequired("shiny gold"))
    }

    @Test
    internal fun partTwo() {
        val bagTypes = parse(readFile())
        val start = Instant.now()
        println(NumberOfBagsRequired(bagTypes).numberOfBagsRequired("shiny gold"))
        println(Duration.between(start, Instant.now()))
    }

    private class NumberOfBagsRequired(bagTypes: List<Bag>) {

        private var bagTypesAsMap: Map<String, Bag>
        private val numberOfBagsRequired = mutableMapOf<Bag, Int>()

        init {
            bagTypesAsMap = bagTypes.map { bagType -> bagType.color to bagType }.toMap()
        }

        fun numberOfBagsRequired(bagKey: String): Int {
            val bag = bagTypesAsMap[bagKey] ?: error("Requested bag not found")
            val numberOfBagsRequiredInBag = numberOfBagsRequired[bag]
            return if (numberOfBagsRequiredInBag != null) {
                numberOfBagsRequiredInBag
            } else {
                val computedNumberOfBagsRequiredInBag = if (bag.bags.isEmpty()) {
                    0
                } else {
                    bag.bags
                            .map { it.value + it.value * this.numberOfBagsRequired(it.key) }
                            .sum()
                }
                numberOfBagsRequired[bag] = computedNumberOfBagsRequiredInBag
                computedNumberOfBagsRequiredInBag
            }
        }

    }

    private fun parse(input: List<String>): List<Bag> {
        val bagTypes = mutableListOf<Bag>()

        val noOtherBags = Regex("(.*?) bags contain no other bags.")
        val withOtherBags = Regex("(.*?) bags contain (.*?).")
        for (line in input) {
            val noOtherBagMatch = noOtherBags.matchEntire(line)
            if (noOtherBagMatch != null) {
                val bagColor = noOtherBagMatch.groupValues[1]
                bagTypes.add(Bag(bagColor, mapOf()))
            } else {
                val withOtherBagMatch = withOtherBags.matchEntire(line)!!
                val bagColor = withOtherBagMatch.groupValues[1]
                val bags = withOtherBagMatch.groupValues[2].split(", ")
                        .map { (it.substring(2, it.length - 4).trim() to it.substring(0, 1).toInt()) }
                        .toMap()
                bagTypes.add(Bag(bagColor, bags))
            }
        }
        return bagTypes
    }

    data class Bag(val color: String, val bags: Map<String, Int>)


    private fun readFile(): List<String> {
        return javaClass.getResource("07").readText().split(System.lineSeparator()).filter { it.isNotEmpty() }
    }
}