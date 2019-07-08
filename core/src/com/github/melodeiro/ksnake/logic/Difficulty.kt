package com.github.melodeiro.ksnake.logic

import kotlin.math.pow

class Difficulty(var trapsToSpawn: Int = 10,
                 val foodsToSpawn: Int = 1,
                 private var speedStep: Int = 7,
                 var turnsToNewTrap: Int = 50,
                 var turnsToPowerUp: Int = 200) {

    private val movingDelays = mutableListOf<Long>()

    init {
        movingDelays.add(1500)
        for (i in 1..11)
            movingDelays.add((1532f / (1.5f.pow(i))).toLong())
    }

    fun increaseSpeed() {
        if (speedStep >= movingDelays.size)
            return

        speedStep++
        println("Speed increased to $speedLevel, current frame delay is $movingDelay")
    }

    fun decreaseSpeed() {
        if (speedStep <= 3)
            return

        speedStep--
        println("Speed decreased to $speedLevel, current frame delay is $movingDelay")
    }

    val speedLevel: Int
        get() = speedStep - 2
    val movingDelay: Long
        get() = movingDelays[speedStep - 1]
}