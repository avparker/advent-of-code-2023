import kotlin.math.max

fun main() {

  val RED_MAX = 12
  val GREEN_MAX = 13
  val BLUE_MAX = 14

  data class Draw(val red: Int, val green: Int, val blue: Int)

  fun Draw.power() = red * green * blue

  data class Game(val id: Int, val draws: List<Draw>)

  fun Draw.isPossible(): Boolean =
    red <= RED_MAX && green <= GREEN_MAX && blue <= BLUE_MAX

  fun Game.isPossible(): Boolean =
    draws.all { it.isPossible() }

  fun Game.power(): Int {
    // Reusing the "Draw" data class to hold the max seen for each color
    val maxColorValues = draws.foldRight(Draw(0, 0, 0)) { curr, new ->
      Draw(max(curr.red, new.red), max(curr.green, new.green), max(curr.blue, new.blue))
    }
    return maxColorValues.power()
  }

  // e.g. 3 blue, 4 red
  // e.g. 1 red, 2 green, 6 blue
  // missing colors get 0 values
  fun parseDraw(draw: String): Draw {
    val colors = draw.split(",")
    val map = hashMapOf<String, Int>()
    colors.forEach {
      val colorCount = it.split(" ").filter { it.isNotBlank() }
      val count = colorCount.first().toInt()
      val color = colorCount.last()
      map[color] = count
    }
    return Draw(red = map["red"] ?: 0, green = map["green"] ?: 0, blue = map["blue"] ?: 0)
  }

  // parse a Game into a data class
  // e.g. Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
  fun parseGame(line: String): Game {
    val parts = line.split(":")
    val gameId = parts.first().substringAfter("Game ").toInt()
    val draws = parts.last().split(";")
    return Game(gameId, draws.map { parseDraw(it) })
  }

  fun gameScore(line: String): Int {
    val game = parseGame(line)
    return if (game.isPossible()) game.id
    else 0
  }

  fun gamePower(line: String): Int {
    val game = parseGame(line)
    return game.power()
  }

  fun part1(input: List<String>): Int {
    return input.sumOf { gameScore(it) }
  }

  fun part2(input: List<String>): Int {
    return input.sumOf { gamePower(it) }
  }

  val testInput = readInput("Day02_test")
  check(gameScore("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green") == 1)
  check(gameScore("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue") == 2)
  check(gameScore("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red") == 0)
  check(gameScore("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red") == 0)
  check(gameScore("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green") == 5)
  check(part1(testInput) == 8)

  check(gamePower("Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green") == 48)
  check(gamePower("Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue") == 12)
  check(gamePower("Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red") == 1560)
  check(gamePower("Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red") == 630)
  check(gamePower("Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green") == 36)
  check(part2(testInput) == 2286)

  val input = readInput("Day02")
  part1(input).println()
  part2(input).println()
}
