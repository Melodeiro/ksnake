package com.github.melodeiro.ksnake.logic.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.github.melodeiro.ksnake.logic.ecs.component.TextureComponent
import com.github.melodeiro.ksnake.logic.ecs.component.TransformComponent
import com.github.melodeiro.ksnake.logic.ecs.component.texture
import com.github.melodeiro.ksnake.logic.ecs.component.transform
import ktx.ashley.allOf
import ktx.graphics.use

class RenderSystem(private val batch: Batch) : IteratingSystem(allOf(TextureComponent::class, TransformComponent::class).get()) {
    override fun update(deltaTime: Float) {
        batch.use {
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity.transform ?: return
        val texture = entity.texture ?: return
        batch.draw(texture, transform.position.x, transform.position.y, transform.width, transform.height)
    }
}