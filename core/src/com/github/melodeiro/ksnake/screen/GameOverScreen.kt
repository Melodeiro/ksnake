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
import ktx.graphics.use

class GameOverScreen(private val app: App,
                     private val game: Game,
                     private val batch: Batch,
                     assets: AssetManager,
                     private val camera: OrthographicCamera) : KtxScreen {

    private val font = assets.get<BitmapFont>("Righteous-Regular.ttf")

    override fun render(delta: Float) {
        camera.update()
        batch.projectionMatrix = camera.combined

        batch.use {
            val text = "[#CC0000FF]GAME OVER[]\n" +
                    "[#FFA500FF]Score: ${game.calculateScore()}[]\n" +
                    "[#CC0000FF]ENTER - reset[]"
            font.draw(it, text, 140f, 300f)
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            app.setScreen<MainMenuScreen>()
            game.reset()
        }
    }
}