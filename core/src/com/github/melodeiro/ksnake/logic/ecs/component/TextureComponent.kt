package com.github.melodeiro.ksnake.logic.ecs.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import ktx.ashley.get
import ktx.ashley.mapperFor

class TextureComponent : Component {
    var texture: Texture? = null

    companion object {
        val mapper = mapperFor<TextureComponent>()
    }
}

inline val Entity.texture: Texture?
    get() = this[TextureComponent.mapper]?.texture