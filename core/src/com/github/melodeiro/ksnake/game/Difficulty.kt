package com.github.melodeiro.ksnake.game

class Difficulty(var trapsToSpawn: Int = 5,
                 private var speedStep: Int = 8,
                 var trapsRespawnEachTurn: Int = 200) {

    private val speedSteps = mutableListOf<Long>()

    init {
        speedSteps.add(500)
        for (i in 1..9)
            speedSteps.add((500f / (i * 1.4285714f)).toLong())
    }

    fun nextSpeed() {
        if (speedStep > speedSteps.size)
            return

        speedStep++
    }

    fun previousSpeed() {
        if (speedStep < 1)
            return

        speedStep--
    }

    val speedLevel = speedStep
    val speed = speedSteps[speedStep - 1]
}