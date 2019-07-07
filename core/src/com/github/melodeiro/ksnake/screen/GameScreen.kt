package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.github.melodeiro.ksnake.App
import com.github.melodeiro.ksnake.logic.Direction
import com.github.melodeiro.ksnake.logic.GameOver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.async.KtxAsync
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
            game.snake.forEach { batch.draw(snakeElementImage, it.x, it.y, it.width, it.height) }
            game.traps.forEach { batch.draw(trapImage, it.x, it.y, it.width, it.height) }
            game.foods.forEach { batch.draw(foodImage, it.x, it.y, it.width, it.height) }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            game.tryDirection(Direction.LEFT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            game.tryDirection(Direction.RIGHT)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            game.tryDirection(Direction.UP)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            game.tryDirection(Direction.DOWN)
        }
    }

    override fun show() {
        game.start()
    }

    override fun dispose() {
        snakeElementImage.dispose()
        foodImage.dispose()
        trapImage.dispose()
    }
}