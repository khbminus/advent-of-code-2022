fun main(args: Array<String>) {
    val days = listOf(
        Day1(),
        Day2(),
        Day3(),
        Day4(),
        Day5(),
        Day6(),
        Day7(),
        Day8(),
        Day9(),
        Day10(),
        Day11(),
        Day12(),
        Day13()
    )
    days.forEachIndexed { index, day ->
        println("---DAY ${index + 1}---")
        println("Answer for part 1 is ${day.part1()}")
        println("Answer for part 2 is ${day.part2()}")
    }
}
