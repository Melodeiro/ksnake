package com.github.melodeiro.ksnake.logic.ecs.system

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.Texture
import com.github.melodeiro.ksnake.logic.Difficulty
import com.github.melodeiro.ksnake.logic.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.log.logger

val log = logger<SpawnSystem>()

class SpawnSystem(private val difficulty: Difficulty) : EntitySystem() {
    private val entities by lazy { engine.getEntitiesFor(allOf(RespawnComponent::class).get()) }
    private val foods by lazy { engine.getEntitiesFor(allOf(FoodComponent::class).get()) }
    private val traps by lazy { engine.getEntitiesFor(allOf(TrapComponent::class).get()) }
    private val randomSpawnSystem by lazy { engine.getSystem(RandomSpawnSystem::class.java) }
    private val gameStateSystem by lazy { engine.getSystem(GameStateSystem::class.java) }

    private var lastStepTrapSpawned = -1L // Spawn at the start
    private var lastStepPowerUpSpawned = 0L // Don't spawn at the start

    override fun update(deltaTime: Float) {
        entities.forEach { entity ->
            entity[RespawnComponent.mapper]?.let {
                if (it.spawnTime + it.timeToRespawn < System.currentTimeMillis()) {
                    engine.removeEntity(entity)
                }
            }
        }
        if (foods.size() < difficulty.foodsToSpawn)
            spawn<FoodComponent>("food.png")
        if (gameStateSystem.currentTurn % difficulty.turnsToNewTrap == 0L && lastStepTrapSpawned != gameStateSystem.currentTurn && traps.size() < difficulty.trapsToSpawn) {
            spawn<TrapComponent>("trap.png")
            lastStepTrapSpawned = gameStateSystem.currentTurn
        }
        if (gameStateSystem.currentTurn % difficulty.turnsToPowerUp == 0L && lastStepPowerUpSpawned != gameStateSystem.currentTurn) {
            spawn<PowerUpComponent>("slow.png")
            lastStepPowerUpSpawned = gameStateSystem.currentTurn
        }
    }

    private inline fun <reified T : Component> spawn(texturePath: String) {
        val entity = engine.entity {
            with<T>()
            with<TransformComponent> {
                setPosition(randomSpawnSystem.getFreePosition())
            }
            with<TextureComponent> {
                texture = Texture(texturePath)
            }
        }

        log.debug { "Entity has spawned at ${entity.transform?.position?.x}, ${entity.transform?.position?.y} with texture $texturePath" }
    }
}