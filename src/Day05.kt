// This is a template that can be used to start each day with a fresh file
fun main() {

  data class RangeMapping(val destRangeStart: Long, val sourceRangeStart: Long, val rangeLength: Long)

  fun RangeMapping.lookup(item: Long): Long? =
    if (sourceRangeStart <= item && item < (sourceRangeStart + rangeLength)) {
      destRangeStart + (item - sourceRangeStart)
    } else null

  fun List<RangeMapping>.lookup(item: Long): Long =
    firstNotNullOfOrNull { it.lookup(item) } ?: item

  data class Almanac(
    val seeds: List<LongRange>,
    val seedToSoil: List<RangeMapping>,
    val soilToFertilizer: List<RangeMapping>,
    val fertilizerToWater: List<RangeMapping>,
    val waterToLight: List<RangeMapping>,
    val lightToTemperature: List<RangeMapping>,
    val temperatureToHumidity: List<RangeMapping>,
    val humidityToLocation: List<RangeMapping>,
  )

  fun Almanac.locationFor(seed: Long): Long {
    val soil = seedToSoil.lookup(seed)
    val fertilizer = soilToFertilizer.lookup(soil)
    val water = fertilizerToWater.lookup(fertilizer)
    val light = waterToLight.lookup(water)
    val temperature = lightToTemperature.lookup(light)
    val humidity = temperatureToHumidity.lookup(temperature)
    val location = humidityToLocation.lookup(humidity)
//    println(" $seed -> $soil, $fertilizer, $water, $light, $temperature, $humidity, $location")
    return location
  }

  fun Almanac.lowestLocation(): Long =
    seeds.minOf {
      it.minOf {
        locationFor(it)
      }
    }

  fun parseRangeMapping(input: String): RangeMapping {
    val parts = input.split(" ").map { it.toLong() }
    return RangeMapping(parts[0], parts[1], parts[2])
  }

  fun parseRangeMappings(input: List<String>): Pair<List<RangeMapping>, List<String>> {
    val useful = input.dropWhile { it.isBlank() || it.endsWith(" map:") }
    val rangeMappings = useful.takeWhile { it.isNotBlank() }.map { parseRangeMapping(it) }
    val remainder = useful.dropWhile { it.isNotBlank() }
    return rangeMappings to remainder
  }

  fun parseAlmanacMappings(seeds: List<LongRange>, input: List<String>): Almanac {
    // This assumes the maps are in the order as per the example!
    val (seedToSoil, rem1) = parseRangeMappings(input)
    val (soilToFertilizer, rem2) = parseRangeMappings(rem1)
    val (fertilizerToWater, rem3) = parseRangeMappings(rem2)
    val (waterToLight, rem4) = parseRangeMappings(rem3)
    val (lightToTemperature, rem5) = parseRangeMappings(rem4)
    val (temperatureToHumidity, rem6) = parseRangeMappings(rem5)
    val (humidityToLocation, _) = parseRangeMappings(rem6)
    return Almanac(
      seeds,
      seedToSoil,
      soilToFertilizer,
      fertilizerToWater,
      waterToLight,
      lightToTemperature,
      temperatureToHumidity,
      humidityToLocation
    )
  }

  fun parseAlmanac1(input: List<String>): Almanac {
    val seeds = input.first().substringAfter("seeds: ").split(" ").map { it.toLong()..it.toLong() }
    return parseAlmanacMappings(seeds, input.drop(1))
  }

  val pairRegex = Regex("(\\d+) (\\d+)")
  fun parseLongPairs(input: String): List<Pair<Long, Long>> {
    val pairs = pairRegex.findAll(input)
    return pairs.map {
      val start = it.groups[1]!!.value.toLong()
      val end = it.groups[2]!!.value.toLong()
      start to end
    }.toList()
  }

  fun parseAlmanac2(input: List<String>): Almanac {
    val pairs = parseLongPairs(input.first().substringAfter("seeds: "))
    val seedRanges = pairs.map { it.first..(it.first+it.second) }
    return parseAlmanacMappings(seedRanges, input.drop(1))
  }

  fun part1(input: List<String>): Long {
    val almanac = parseAlmanac1(input)
    return almanac.lowestLocation()
  }

  fun part2(input: List<String>): Long {
    val almanac = parseAlmanac2(input)
    return almanac.lowestLocation()
  }

  val rangeMapping1 = RangeMapping(50, 98, 2)
  val rangeMapping2 = RangeMapping(52, 50, 48)
  check(rangeMapping2.lookup(49) == null)
  check(rangeMapping2.lookup(50) == 52L)
  check(rangeMapping2.lookup(51) == 53L)
  check(rangeMapping2.lookup(96) == 98L)
  check(rangeMapping2.lookup(97) == 99L)
  check(rangeMapping2.lookup(98) == null)

  val rangeMappings1 = listOf(rangeMapping1, rangeMapping2)
  check(rangeMappings1.lookup(10) == 10L)
  check(rangeMappings1.lookup(49) == 49L)
  check(rangeMappings1.lookup(50) == 52L)
  check(rangeMappings1.lookup(51) == 53L)
  check(rangeMappings1.lookup(96) == 98L)
  check(rangeMappings1.lookup(97) == 99L)
  check(rangeMappings1.lookup(98) == 50L)
  check(rangeMappings1.lookup(99) == 51L)
  check(rangeMappings1.lookup(100) == 100L)

  val testInput = readInput("Day05_test")
  val testAlmanac1 = parseAlmanac1(testInput)
  check(testAlmanac1.locationFor(79) == 82L)
  check(testAlmanac1.locationFor(14) == 43L)
  check(testAlmanac1.locationFor(55) == 86L)
  check(testAlmanac1.locationFor(13) == 35L)
  check(testAlmanac1.lowestLocation() == 35L)

  val testAlmanac2 = parseAlmanac2(testInput)
  check(testAlmanac2.lowestLocation() == 46L)

  check(part1(testInput) == 35L)
  check(part2(testInput) == 46L)

  // TODO - there must be some pre-processing we can do because running part2 takes a very long time with this implementation.
  val input = readInput("Day05")
  println(part1(input))
  println(part2(input))
}