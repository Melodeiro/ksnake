@file:JvmName("DesktopLauncher")

package com.github.melodeiro.ksnake.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.melodeiro.ksnake.App

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(App(), config)
}
