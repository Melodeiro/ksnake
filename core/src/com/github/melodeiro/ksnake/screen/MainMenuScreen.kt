package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.github.melodeiro.ksnake.Game
import ktx.app.KtxScreen

class MainMenuScreen(private val game: Game) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, game.cameraWidth, game.cameraHeight) }

    override fun render(delta: Float) {
        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.draw(game.batch, "Welcome to KSnake!", 100f, 150f)
        game.font.draw(game.batch, "Press arrow keys to begin", 100f, 100f)
        game.batch.end()

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            println("mainmenu bug")
            game.addScreen(GameScreen(game))
            game.setScreen<GameScreen>()
            game.removeScreen<MainMenuScreen>()
            dispose()
        }
    }
}