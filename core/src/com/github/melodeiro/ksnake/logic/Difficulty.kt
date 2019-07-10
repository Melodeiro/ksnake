package com.github.melodeiro.ksnake.logic

import com.github.melodeiro.ksnake.logic.entity.PowerUp
import ktx.log.logger
import kotlin.math.pow

private val log = logger<Difficulty>()

class Difficulty(var trapsToSpawn: Int = 10,
                 val foodsToSpawn: Int = 1,
                 private var speedStep: Int = 7,
                 var turnsToNewTrap: Int = 50,
                 var turnsToPowerUp: Int = 300,
                 val startingSnakeSize: Int = 4) {

    private val movingDelays = mutableListOf<Long>()

    val speedLevel: Int
        get() = speedStep - 2

    init {
        movingDelays.add(1500)
        for (i in 1..14) {
            val movingDelay = (1532f / (1.5f.pow(i))).toLong()
            movingDelays.add(movingDelay)
        }
        log.debug {
            var result = "Generated delays:\n"
            movingDelays.forEach { result += "$it\n" }
            result
        }
    }

    fun getMovingDelay(currentPowerUp: PowerUp): Long {
        val tmpStep = if (currentPowerUp.isActive) speedStep + currentPowerUp.type.speedShift else speedStep
        return when {
            tmpStep < 0 -> movingDelays[0]
            tmpStep > movingDelays.size - 1 -> movingDelays[movingDelays.size - 1]
            else -> movingDelays[tmpStep]
        }
    }

    fun increaseSpeed() {
        if (speedStep >= movingDelays.size - 3)
            return

        speedStep++
        log.debug { "Speed increased to $speedLevel, current frame delay is ${getMovingDelay(PowerUp.none())}" }
    }

    fun decreaseSpeed() {
        if (speedStep <= 3)
            return

        speedStep--
        log.debug { "Speed decreased to $speedLevel, current frame delay is ${getMovingDelay(PowerUp.none())}" }
    }
}