fun main() {
  data class Card(val winningNumbers: Set<Int>, val actualNumbers: Set<Int>)

  fun Card.score(): Int {
    val matches = winningNumbers.intersect(actualNumbers).size
    return if (matches > 0) Math.pow(2.0, (matches - 1).toDouble()).toInt()
    else 0
  }

  fun parseCard(input: String): Card {
    val parts = input.split(":")
    val groups = parts.last().split("|")
    return Card(
      groups.first().split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet(),
      groups.last().split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()
    )
  }

  fun parseCards(input: List<String>): List<Card> =
    input.map(::parseCard)

  fun part1(input: List<String>): Int {
    val cards = parseCards(input)
    return cards.sumOf { it.score() }
  }

  fun part2(input: List<String>): Int {
    return 0
  }

  val testInput = readInput("Day04_test")

  // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
  val card1 = Card(setOf(41, 48, 83, 86, 17), setOf(83, 86, 6, 31, 17, 9, 48, 53))
  card1.score().println()
  println(part1(testInput))
  println(part2(testInput))

  val input = readInput("Day04")
  println(part1(input))
}