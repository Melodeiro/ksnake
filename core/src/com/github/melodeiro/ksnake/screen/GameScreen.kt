package com.github.melodeiro.ksnake.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.logic.SnakeElement
import com.github.melodeiro.ksnake.logic.WorldSettings
import com.github.melodeiro.ksnake.logic.ecs.component.SnakeElementComponent
import com.github.melodeiro.ksnake.logic.ecs.component.SnakeHeadComponent
import com.github.melodeiro.ksnake.logic.ecs.component.TextureComponent
import com.github.melodeiro.ksnake.logic.ecs.component.TransformComponent
import com.github.melodeiro.ksnake.logic.ecs.system.GameStateSystem
import com.github.melodeiro.ksnake.logic.ecs.system.RandomSpawnSystem
import com.github.melodeiro.ksnake.logic.ecs.system.RenderSystem
import com.github.melodeiro.ksnake.logic.ecs.system.SpawnSystem
import kotlinx.coroutines.Job
import ktx.app.KtxScreen
import ktx.ashley.add
import ktx.ashley.entity
import ktx.graphics.color

class GameScreen(private val app: App,
                 private val game: Game,
                 private val batch: Batch,
                 assets: AssetManager,
                 private val engine: PooledEngine,
                 private val camera: OrthographicCamera) : KtxScreen {

    private val fieldBackgroundImage = Texture("field_background.png")
    private val font = assets.get<BitmapFont>("Righteous-Regular.ttf")

    private val worldSettings = WorldSettings(0f, 0f, 480f, 480f, 16f)

    private var gameJob: Job? = null

    init {
        font.color = color(0f, 0.5f, 0f)
    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        batch.projectionMatrix = camera.combined

        // Draw all textures
        /*batch.use { batch ->
            font.draw(batch, "SCORE: ${game.calculateScore()}", game.field.x + 17f, game.field.maxY + font.lineHeight)
            font.draw(batch, "PU: ${game.getCurrentPUInfo()}", game.field.x + 250f, game.field.maxY + font.lineHeight)
            batch.draw(fieldBackgroundImage, game.field.x, game.field.y, game.field.width, game.field.height)
            game.snake.forEach { batch.draw(snakeElementImage, it.x, it.y, it.width, it.height) }
            game.traps.forEach { batch.draw(trapImage, it.x, it.y, it.width, it.height) }
            game.foods.forEach { batch.draw(foodImage, it.x, it.y, it.width, it.height) }
            game.powerUps.forEach {
                val puImage = when(it.type) {
                    PowerUp.Type.SLOW -> slowImage
                    PowerUp.Type.NONE -> throw IllegalStateException("Tried to render spawned NONE power up")
                }
                batch.draw(puImage, it.x, it.y, it.width, it.height)
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            game.tryDirection(Direction.LEFT)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            game.tryDirection(Direction.RIGHT)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            game.tryDirection(Direction.UP)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            game.tryDirection(Direction.DOWN)
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.activateCurrentPU()
        }*/

        engine.update(delta)
    }

    override fun show() {
        engine.add {
            addSystem(RandomSpawnSystem(worldSettings))
            addSystem(SpawnSystem(game.difficulty))
            addSystem(RenderSystem(batch))
            addSystem(GameStateSystem())
        }

        for (i in 0 until game.difficulty.startingSnakeSize) {
            engine.entity {
                with<SnakeElementComponent>()
                with<TransformComponent> {
                    setPosition(worldSettings.x + 128f + (i * worldSettings.cellSize), worldSettings.y + 128f)
                }
                with<TextureComponent> {
                    texture = Texture("snake_element.png")
                }
                if (i == game.difficulty.startingSnakeSize - 1)
                    with<SnakeHeadComponent>()
            }
        }
    }

    override fun dispose() {
        fieldBackgroundImage.dispose()
    }
}