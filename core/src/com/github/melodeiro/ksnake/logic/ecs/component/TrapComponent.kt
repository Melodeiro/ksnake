package com.github.melodeiro.ksnake.logic.ecs.component

import com.badlogic.ashley.core.Component
import ktx.ashley.mapperFor

class TrapComponent : Component {
    companion object {
        val mapper = mapperFor<TrapComponent>()
    }
}
