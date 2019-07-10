package com.github.melodeiro.ksnake.logic.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.github.melodeiro.ksnake.logic.WorldSettings
import com.github.melodeiro.ksnake.logic.ecs.component.TransformComponent
import com.github.melodeiro.ksnake.logic.ecs.component.transform
import ktx.ashley.allOf

class RandomSpawnSystem(private val worldSettings: WorldSettings) : EntitySystem() {
    private val entities by lazy { engine.getEntitiesFor(allOf(TransformComponent::class).get()) }

    private fun getRandomCellPos(start: Float, end: Float): Float = MathUtils.random((start / worldSettings.cellSize).toInt(),
                                                                                     (end / worldSettings.cellSize).toInt()) * worldSettings.cellSize

    private fun getRandomPosition() = Vector2(getRandomCellPos(worldSettings.x, worldSettings.maxX - worldSettings.cellSize),
                                              getRandomCellPos(worldSettings.y, worldSettings.maxY - worldSettings.cellSize))

    fun getFreePosition(): Vector2 {
        val newPos = getRandomPosition()
        if (entities.any { it.transform?.position?.x == newPos.x && it.transform?.position?.y == newPos.y })
            getFreePosition()
        return newPos
    }
}