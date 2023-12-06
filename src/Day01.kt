fun main() {

    fun lineCalibration(line: String): Int {
        val digits = line.filter { it.isDigit() }
        return "${digits.first()}${digits.last()}".toInt()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf {  lineCalibration(it) }
    }

    fun lineCalibration2(line: String): Int {
        // NOTE: This replaces all the digits and words in the string.
        // We don't need to do this, we only need the first and last number
        // So there is a much more efficient way to do this
        val replaced = line.mapIndexed{ i, c ->
            if (c.isDigit()) c
            else {
                // This is ugly, should find a better way to do this
                // Note we can't just do replacement in order
                // e.g. "eightwo" would first get turned into "eigh2" and the eight would be missed
                val remaining = line.substring(i, (i+5).coerceAtMost(line.length))
                if (remaining.startsWith("one") ) "1"
                else if (remaining.startsWith("two") ) "2"
                else if (remaining.startsWith("three") ) "3"
                else if (remaining.startsWith("four") ) "4"
                else if (remaining.startsWith("five") ) "5"
                else if (remaining.startsWith("six") ) "6"
                else if (remaining.startsWith("seven") ) "7"
                else if (remaining.startsWith("eight") ) "8"
                else if (remaining.startsWith("nine") ) "9"
                else ""
            }
        }.toString()
        return lineCalibration(replaced)
    }
    fun part2(input: List<String>): Int {
        return input.sumOf {  lineCalibration2(it) }
    }

    val testInput = readInput("Day01_test")
    check(lineCalibration("1abc2") == 12)
    check(lineCalibration("pqr3stu8vwx") == 38)
    check(lineCalibration("a1b2c3d4e5f") == 15)
    check(lineCalibration("treb7uchet") == 77)
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_test2")
    check(part2(listOf("two1nine")) == 29)
    check(part2(listOf("eightwothree")) == 83)
    check(part2(listOf("abcone2threexyz")) == 13)
    check(part2(listOf("xtwone3four")) == 24)
    check(part2(listOf("4nineeightseven2")) == 42)
    check(part2(listOf("zoneight234")) == 14)
    check(part2(listOf("7pqrstsixteen")) == 76)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
