package com.github.melodeiro.ksnake

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Array
import com.github.melodeiro.ksnake.logic.Difficulty
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class App : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val font by lazy { BitmapFont() }
    val cameraWidth = 640f
    val cameraHeight = 480f
    val game = Game(this)

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
}
