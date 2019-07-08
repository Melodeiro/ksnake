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

    override fun show() {
        app.font.color = color(0f, 0.7f, 0f)
        app.font.data.markupEnabled = true
    }

    override fun render(delta: Float) {
        camera.update()
        app.batch.projectionMatrix = camera.combined

        app.batch.use {
            val textX = 30f
            val text = "Welcome to KSnake\n" +
                    "Press [#FFA500FF]RIGHT ARROW[] key to begin\n" +
                    "Current difficulty level: [#CC0000FF]${difficulty.speedLevel}[]\n" +
                    "[#FFA500FF]UP ARROW[] - increase difficulty\n" +
                    "[#FFA500FF]DOWN ARROW[] - decrease difficulty\n" +
                    "[#FFA500FF]SPACE[] - activate Power Up"
            app.font.draw(it, text, textX, 365f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            difficulty.increaseSpeed()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            difficulty.decreaseSpeed()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            app.addScreen(GameScreen(app))
            app.setScreen<GameScreen>()
            app.removeScreen<MainMenuScreen>()
            dispose()
        }
    }
}