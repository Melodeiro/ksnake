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

    override fun render(delta: Float) {
        camera.update()
        app.batch.projectionMatrix = camera.combined

        app.batch.use {
            val text = "[#CC0000FF]GAME OVER[]\n" +
                    "[#FFA500FF]Score: ${app.game.calculateScore()}[]\n" +
                    "[#CC0000FF]ENTER - restart[]"
            app.font.draw(it, text, 140f, 300f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            app.addScreen(MainMenuScreen(app))
            app.setScreen<MainMenuScreen>()
            app.removeScreen<GameOverScreen>()
            app.game.restart()
            dispose()
        }
    }
}