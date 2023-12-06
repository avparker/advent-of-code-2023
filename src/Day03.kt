fun main() {

  data class Symbol(val symbol: Char, val index: Int)
  data class Part(val number: Int, val startIndex: Int, val endIndex: Int)
  data class Line(val possibleParts: List<Part>, val symbols: List<Symbol>)

  // returns true if the part has a symbol adjacent to it
  fun Line.hasAdjacentSymbol(part: Part): Boolean =
    symbols.filter { it.index >= part.startIndex - 1 && it.index <= part.endIndex + 1 }.isNotEmpty()

  // find the parts that are adjacent to the symbol
  fun Line.adjacentParts(symbol: Symbol): List<Part> =
    possibleParts.filter { symbol.index >= it.startIndex - 1 && symbol.index <= it.endIndex + 1 }

  data class Schematic(val lines: List<Line>)

  fun Schematic.getParts(): List<Part> =
    lines.flatMapIndexed { lineIndex, line ->
      line.possibleParts.filter { possibleParts ->
        // find any parts that are adjacent to symbols
        // we check the previous and next line as well as the line itself
        if (lineIndex >= 1)
          lines[lineIndex - 1].hasAdjacentSymbol(possibleParts)
        else if (lineIndex < lines.size - 1)
          lines[lineIndex + 1].hasAdjacentSymbol(possibleParts)
        else line.hasAdjacentSymbol(possibleParts)
      }
    }

  fun Schematic.getGears(): List<Pair<Part, Part>> =
    lines.flatMapIndexed { lineIndex, line ->
      line.symbols.filter { it.symbol == '*' }
        // find all the parts that are adjacent to the symbol
        .map { symbol ->
          val prevLine =
            if (lineIndex >= 1)
              lines[lineIndex - 1].adjacentParts(symbol)
            else emptyList()
          val currLine = line.adjacentParts(symbol)
          val nextLine = if (lineIndex < lines.size - 1)
            lines[lineIndex + 1].adjacentParts(symbol)
          else emptyList()
          (prevLine + currLine + nextLine)
        }.filter { it.size == 2 } // a gear must have exactly 2 parts
        .map {
          it.first() to it.last()
        }
    }

  // e.g. 617*......
  // e.g. ..35...633
  fun parseLine(line: String): Line {
    val regex = Regex("\\d+")
    val matches = regex.findAll(line)
    val possibleParts: List<Part> = buildList {
      matches.forEach { match ->
        add(Part(match.value.toInt(), match.range.first, match.range.last))
      }
    }
    val symbols = line.mapIndexed { index, c ->
      if (c.isDigit() || c == '.') null
      else Symbol(c, index)
    }.filterNotNull()
    return Line(possibleParts, symbols)
  }

  fun parseSchematic(input: List<String>): Schematic {
    return Schematic(input.map { parseLine(it) })
  }

  fun part1(input: List<String>): Int {
    val schematic = parseSchematic(input)
    return schematic.getParts().sumOf { it.number }
  }

  fun part2(input: List<String>): Int {
    val schematic = parseSchematic(input)
    val gears = schematic.getGears()
    return gears.sumOf { it.first.number * it.second.number }
  }

  val testInput = readInput("Day03_test")
  val part617 = Part(617, 0, 2)
  check(parseLine("617*......") == Line(listOf(part617), listOf(Symbol('*', 3))))

  check(Schematic(listOf(Line(listOf(part617), listOf(Symbol('*', 3))))).getParts() == listOf(part617))
  val part35 = Part(35, 2, 3)
  val part633 = Part(633, 6, 8)
  check(parseLine("..35..633") == Line(listOf(part35, part633), emptyList()))
  check(Schematic(listOf(Line(listOf(part35, part633), emptyList()))).getParts() == emptyList<Part>())
  check(part1(testInput) == 4361)

  check(part2(testInput) == 467835)

  val input = readInput("Day03")
  println(part1(input))
  println(part2(input))
}