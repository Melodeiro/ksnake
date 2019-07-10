package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.App
import com.github.melodeiro.ksnake.logic.Game
import ktx.app.KtxScreen
import ktx.graphics.color
import ktx.graphics.use

class MainMenuScreen(private val app: App,
                     private val game: Game,
                     private val batch: Batch,
                     private val assets: AssetManager,
                     private val camera: OrthographicCamera) : KtxScreen {
    private val difficulty = game.difficulty
    private val font = assets.get<BitmapFont>("Righteous-Regular.ttf")

    override fun show() {
        font.color = color(0f, 0.7f, 0f)
        font.data.markupEnabled = true
    }

    override fun render(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.use {
            val textX = 30f
            val text = "Welcome to KSnake\n" +
                    "Press [#FFA500FF]RIGHT ARROW[] key to begin\n" +
                    "Current difficulty level: [#CC0000FF]${difficulty.speedLevel}[]\n" +
                    "[#FFA500FF]UP ARROW[] - increase difficulty\n" +
                    "[#FFA500FF]DOWN ARROW[] - decrease difficulty\n" +
                    "[#FFA500FF]SPACE[] - activate Power Up"
            font.draw(it, text, textX, 365f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            difficulty.increaseSpeed()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            difficulty.decreaseSpeed()
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            app.addScreen(GameScreen(app, game, batch, assets, camera))
            app.setScreen<GameScreen>()
            app.removeScreen<MainMenuScreen>()
            dispose()
        }
    }
}