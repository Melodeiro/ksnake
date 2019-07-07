package com.github.melodeiro.ksnake

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class App : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val cameraWidth = 640f
    val cameraHeight = 480f
    val game = Game(this)

    override fun create() {
        addScreen(MainMenuScreen(this))
        setScreen<MainMenuScreen>()
        super.create()
    }

    override fun dispose() {
        batch.dispose()
        super.dispose()
    }
}
