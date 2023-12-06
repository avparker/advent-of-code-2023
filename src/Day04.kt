fun main() {
  data class Card(val winningNumbers: Set<Int>, val actualNumbers: Set<Int>)

  fun Card.matchCount(): Int {
    return winningNumbers.intersect(actualNumbers).size
  }

  fun Card.score1(): Int {
    val matches = matchCount()
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
    return cards.sumOf { it.score1() }
  }

  fun calculateCardCount(matchCounts: List<Int>): Int {
    // we start with 1 of each card
    val cardCounts = matchCounts.map { 1 }.toIntArray()
    matchCounts.forEachIndexed { index, matchCount ->
      // add the card count for the current card to each of the next matchCount cards
      val currentCount = cardCounts[index]
      for (i in index+1..(index+matchCount).coerceAtMost(matchCounts.size)) {
        cardCounts[i] += currentCount
      }
    }
    // now just count the number of cards
    return cardCounts.sum()
  }

  fun part2(input: List<String>): Int {
    val cards = parseCards(input)
    val cardMatchCount = cards.map { it.matchCount() }
    return calculateCardCount(cardMatchCount)
  }

  val card1 = parseCard("Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53")
  check(card1 == Card(setOf(41, 48, 83, 86, 17), setOf(83, 86, 6, 31, 17, 9, 48, 53)))
  check(card1.score1() == 8)

  val card2 = parseCard("Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19")
  check(card2 == Card(setOf(13, 32, 20, 16, 61), setOf(61, 30, 68, 82, 17, 32, 24, 19)))
  check(card2.score1() == 2)

  val matchCounts = listOf(4, 2, 2, 1, 0, 0)
  check(calculateCardCount(matchCounts) == 30)

  val testInput = readInput("Day04_test")
  check(part1(testInput) == 13)
  check(part2(testInput) == 30)

  val input = readInput("Day04")
  println("part1 ${part1(input)}")
  println("part2 ${part2(input)}")
}