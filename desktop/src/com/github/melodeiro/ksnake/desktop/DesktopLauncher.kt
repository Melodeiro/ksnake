@file:JvmName("DesktopLauncher")

package com.github.melodeiro.ksnake.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.melodeiro.ksnake.App

fun main() {
    val config = LwjglApplicationConfiguration()
    config.title = "KSnake"
    config.width = 480
    config.height = 525
    config.resizable = false
    LwjglApplication(App(), config)
}
