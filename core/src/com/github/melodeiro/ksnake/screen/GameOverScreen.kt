package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.github.melodeiro.ksnake.Game
import ktx.app.KtxScreen



class GameOverScreen(private val game: Game) : KtxScreen {
    private val camera = OrthographicCamera().apply { setToOrtho(false, game.cameraWidth, game.cameraHeight) }
    private val gameOverFont by lazy { BitmapFont() }
    private val finalScoreFont by lazy { BitmapFont() }

    init {
        gameOverFont.color = Color.RED
        gameOverFont.data.setScale(2f)
        finalScoreFont.color = Color.GREEN
        finalScoreFont.data.setScale(2f)
    }

    override fun render(delta: Float) {
        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        gameOverFont.draw(game.batch, "GAME OVER", 230f, 300f)
        finalScoreFont.draw(game.batch, "Score: ${game.calculateScore()}", 230f, 250f)
        game.batch.end()
    }
}