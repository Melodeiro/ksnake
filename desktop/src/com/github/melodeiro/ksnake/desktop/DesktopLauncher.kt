@file:JvmName("DesktopLauncher")

package com.github.melodeiro.ksnake.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.github.melodeiro.ksnake.Game

fun main(args: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(Game(), config)
}
