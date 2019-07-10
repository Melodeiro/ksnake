package com.github.melodeiro.ksnake.logic.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class RespawnComponent(secondsToDespawn: Int) : Component {
    companion object {
        val mapper = mapperFor<RespawnComponent>()
    }

    var isRespawnNeeded = false

    val timeToRespawn = secondsToDespawn * 1000L
    val spawnTime = System.currentTimeMillis()
}