package com.github.melodeiro.ksnake.logic.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import ktx.ashley.get
import ktx.ashley.mapperFor

class TransformComponent(x: Float = 0f, y: Float = 0f, var width: Float = 16f, var height: Float = 16f) : Component {
    val position = Vector2(x, y)

    companion object {
        val mapper = mapperFor<TransformComponent>()
    }

    fun setPosition(position: Vector2) {
        this.position.x = position.x
        this.position.y = position.y
    }

    fun setPosition(x: Float, y: Float) {
        this.position.x = x
        this.position.y = y
    }
}

inline val Entity.transform: TransformComponent?
    get() = this[TransformComponent.mapper]