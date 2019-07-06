package com.github.melodeiro.ksnake

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.melodeiro.ksnake.game.Difficulty
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Game : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val font by lazy { BitmapFont() }
    val cameraWidth = 640f
    val cameraHeight = 480f
    val difficulty = Difficulty()
    var moves = 0
    var foodAteAmount = 0

    override fun create() {
        font.color = Color.GREEN
        font.data.setScale(1f, 1.3f)

        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        super.dispose()
    }

    fun calculateScore(): Int {
        val tempScores = foodAteAmount * 100f * difficulty.speedLevel - moves
        return if (tempScores < 0) 0 else tempScores.toInt()
    }
}
