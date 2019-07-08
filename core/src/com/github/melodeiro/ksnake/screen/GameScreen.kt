package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import com.github.melodeiro.ksnake.logic.Direction
import com.github.melodeiro.ksnake.logic.entities.PowerUp
import ktx.app.KtxScreen
import ktx.graphics.color
import ktx.graphics.use

class GameScreen(private val app: App) : KtxScreen {
    private val game = app.game

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera()
            .apply { setToOrtho(false, app.cameraWidth, app.cameraHeight) }

    private val snakeElementImage = Texture("snake_element.png")
    private val foodImage = Texture("food.png")
    private val trapImage = Texture("trap.png")
    private val slowImage = Texture("slow.png")
    private val fieldBackgroundImage = Texture("field_background.png")
    private val mainFont = app.assetManager.get<BitmapFont>("Righteous-Regular.ttf")

    init {
        mainFont.color = color(0f, 0.5f, 0f)
    }

    override fun render(delta: Float) {
        if (!game.isRunning) {
            app.addScreen(GameOverScreen(app))
            app.setScreen<GameOverScreen>()
            app.removeScreen<GameScreen>()
            dispose()
        }

        // generally good practice to update the camera's matrices once per frame
        camera.update()
        app.batch.projectionMatrix = camera.combined

        // Draw all textures
        app.batch.use { batch ->
            mainFont.draw(batch, "SCORE: ${game.calculateScore()}", game.field.x + 17f, game.field.maxY + mainFont.lineHeight)
            mainFont.draw(batch, "PU: ${game.getCurrentPUInfo()}", game.field.x + 250f, game.field.maxY + mainFont.lineHeight)
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
        game.start()
    }

    override fun dispose() {
        snakeElementImage.dispose()
        fieldBackgroundImage.dispose()
        foodImage.dispose()
        trapImage.dispose()
        slowImage.dispose()
    }
}