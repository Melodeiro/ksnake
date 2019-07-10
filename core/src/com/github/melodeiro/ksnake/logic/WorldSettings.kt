package com.github.melodeiro.ksnake.logic

class WorldSettings(val x: Float, val y: Float, val width: Float, val height: Float, val cellSize: Float) {
    val maxX: Float
        get() = x + width
    val maxY: Float
        get() = y + height
}