package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import com.github.melodeiro.ksnake.logic.Direction
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.logic.GameOver
import com.github.melodeiro.ksnake.logic.component.Position
import com.github.melodeiro.ksnake.logic.entity.PowerUp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.async.KtxAsync
import ktx.graphics.color
import ktx.graphics.use

class GameScreen(private val app: App,
                 private val game: Game,
                 private val batch: Batch,
                 private val assets: AssetManager,
                 private val camera: OrthographicCamera) : KtxScreen {
    private val snakeElementImage = Texture("snake_element.png")
    private val foodImage = Texture("food.png")
    private val trapImage = Texture("trap.png")
    private val slowImage = Texture("slow.png")
    private val fieldBackgroundImage = Texture("field_background.png")
    private val font = assets.get<BitmapFont>("Righteous-Regular.ttf")

    private var gameJob: Job? = null

    init {
        font.color = color(0f, 0.5f, 0f)
    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        batch.projectionMatrix = camera.combined

        // Draw all textures
        batch.use { batch ->
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
        }
    }

    override fun show() {
        gameJob = KtxAsync.launch {
            try {
                game.runGameLoop()
            } catch (e: GameOver) {
                app.addScreen(GameOverScreen(app, game, batch, assets, camera))
                app.setScreen<GameOverScreen>()
                app.removeScreen<GameScreen>()
                dispose()
            }
        }
    }

    override fun dispose() {
        gameJob?.cancel()
        snakeElementImage.dispose()
        fieldBackgroundImage.dispose()
        foodImage.dispose()
        trapImage.dispose()
        slowImage.dispose()
    }
}