package com.github.melodeiro.ksnake

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.github.melodeiro.ksnake.logic.Game
import com.github.melodeiro.ksnake.screen.MainMenuScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.freetype.loadFreeTypeFont
import ktx.freetype.registerFreeTypeFontLoaders
import ktx.inject.Context
import ktx.log.logger

private val log = logger<App>()

class App : KtxGame<KtxScreen>() {
    private val cameraWidth = 480f
    private val cameraHeight = 525f
    private val assetManager = AssetManager()
    private val context = Context()

    override fun create() {
        assetManager.registerFreeTypeFontLoaders()
        assetManager.loadFreeTypeFont("Righteous-Regular.ttf") {
            size = 25
        }
        assetManager.finishLoading()
        Gdx.app.debug("kek", "top")

        context.register {
            bindSingleton(this@App)
            bindSingleton<Batch>(SpriteBatch())
            bindSingleton(assetManager)
            bindSingleton(OrthographicCamera().apply { setToOrtho(false, cameraWidth, cameraHeight) })
            bindSingleton(PooledEngine())
            bindSingleton(Game())
            addScreen(MainMenuScreen(inject(), inject(), inject(), inject(), inject()))
        }

        setScreen<MainMenuScreen>()
        super.create()
    }

    override fun dispose() {
        log.debug { "Entities in engine: ${context.inject<PooledEngine>().entities.size()}" }
        context.dispose()
        super.dispose()
    }
}
