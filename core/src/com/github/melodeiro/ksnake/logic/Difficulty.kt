package com.github.melodeiro.ksnake.logic

class Difficulty(var trapsToSpawn: Int = 10,
                 val foodsToSpawn: Int = 1,
                 private var speedStep: Int = 5,
                 var turnsToNewTrap: Int = 50) {

    private val speedSteps = mutableListOf<Long>()

    init {
        speedSteps.add(500)
        for (i in 1..9)
            speedSteps.add((500f / (i * 1.75f)).toLong())
    }

    fun increaseSpeed() {
        if (speedStep >= speedSteps.size)
            return

        speedStep++
        println("Speed increased to $speedLevel, current frame delay is $speed")
    }

    fun decreaseSpeed() {
        if (speedStep <= 1)
            return

        speedStep--
        println("Speed decreased to $speedLevel, current frame delay is $speed")
    }

    val speedLevel: Int
        get() = speedStep
    val speed: Long
        get() = speedSteps[speedStep - 1]
}