package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import ktx.app.KtxScreen
import ktx.graphics.color
import ktx.graphics.use


class GameOverScreen(private val app: App) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, app.cameraWidth, app.cameraHeight) }
    private val gameOverFont by lazy { BitmapFont() }
    private val finalScoreFont by lazy { BitmapFont() }

    init {
        gameOverFont.color = color(1f, 0f, 0f)
        gameOverFont.data.setScale(2f)
        finalScoreFont.color = color(1f, 1f, 0f)
        finalScoreFont.data.setScale(2f)
    }

    override fun render(delta: Float) {
        camera.update()
        app.batch.projectionMatrix = camera.combined

        app.batch.use {
            gameOverFont.draw(it, "GAME OVER", 230f, 300f)
            finalScoreFont.draw(it, "Score: ${app.game.calculateScore()}", 230f, 250f)
            gameOverFont.draw(it, "ENTER - restart", 230f, 200f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            app.addScreen(MainMenuScreen(app))
            app.setScreen<MainMenuScreen>()
            app.removeScreen<GameOverScreen>()
            app.game.restart()
            dispose()
        }
    }

    override fun dispose() {
        gameOverFont.dispose()
        finalScoreFont.dispose()
    }
}