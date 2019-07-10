package com.github.melodeiro.ksnake.logic.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class PowerUpComponent : Component {
    val mapper = mapperFor<PowerUpComponent>()
}
