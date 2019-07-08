package com.github.melodeiro.ksnake

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import com.sun.deploy.association.utility.AppConstants
import ktx.app.KtxGame
import ktx.app.KtxScreen

class App : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val cameraWidth = 800f
    val cameraHeight = 600f
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
