package com.github.melodeiro.ksnake

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders

class App : KtxGame<KtxScreen>() {
    val batch by lazy { SpriteBatch() }
    val cameraWidth = 480f
    val cameraHeight = 525f
    val game = Game(this)
    val assetManager = AssetManager()
    val font: BitmapFont by lazy { assetManager.get<BitmapFont>("Righteous-Regular.ttf") }

    override fun create() {
        assetManager.registerFreeTypeFontLoaders()
        assetManager.loadFreeTypeFont("Righteous-Regular.ttf") {
            size = 25
        }
        assetManager.finishLoading()
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
