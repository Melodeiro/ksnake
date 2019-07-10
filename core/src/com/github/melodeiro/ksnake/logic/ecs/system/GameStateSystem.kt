package com.github.melodeiro.ksnake.logic.ecs.system

import com.badlogic.ashley.core.EntitySystem

class GameStateSystem : EntitySystem() {
    var currentTurn = 0L
        private set

    fun nextTurn() {
        currentTurn++
    }
}