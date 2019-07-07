package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.melodeiro.ksnake.App
import ktx.app.KtxScreen
import ktx.graphics.use

class MainMenuScreen(private val app: App) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, app.cameraWidth, app.cameraHeight) }
    private val game = app.game

    override fun render(delta: Float) {
        camera.update()
        app.batch.projectionMatrix = camera.combined

        app.batch.use {
            app.font.draw(it, "Welcome to KSnake!", 100f, 150f)
            app.font.draw(it, "Press arrow keys to begin", 100f, 100f)
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            app.addScreen(GameScreen(app))
            app.setScreen<GameScreen>()
            app.removeScreen<MainMenuScreen>()
            dispose()
        }
    }
}