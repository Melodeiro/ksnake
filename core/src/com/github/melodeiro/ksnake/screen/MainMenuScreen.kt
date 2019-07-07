package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import ktx.app.KtxScreen
import ktx.graphics.color
import ktx.graphics.use

class MainMenuScreen(private val app: App) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, app.cameraWidth, app.cameraHeight) }
    private val difficulty = app.game.difficulty
    private val font by lazy { BitmapFont() }

    override fun show() {
        font.data.setScale(2f)
        font.color = color(0f, 1f, 0f)
    }

    override fun render(delta: Float) {
        camera.update()
        app.batch.projectionMatrix = camera.combined

        app.batch.use {

            font.draw(it, "Welcome to KSnake", 130f, 400f)
            font.draw(it, "Press RIGHT ARROW key to begin", 130f, 350f)
            font.draw(it, "Current difficulty level: ${difficulty.speedLevel}", 130f, 300f)
            font.draw(it, "UP ARROW - increase difficulty", 130f, 250f)
            font.draw(it, "DOWN ARROW - decrease difficulty", 130f, 200f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            difficulty.increaseSpeed()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            difficulty.decreaseSpeed()
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            app.addScreen(GameScreen(app))
            app.setScreen<GameScreen>()
            app.removeScreen<MainMenuScreen>()
            dispose()
        }
    }

    override fun dispose() {
        font.dispose()
    }
}