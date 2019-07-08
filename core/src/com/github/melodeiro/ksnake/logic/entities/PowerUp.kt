package com.github.melodeiro.ksnake.logic.entities

class PowerUp(val type: Type, val x: Float, val y: Float, val width: Float, val height: Float) {

    var isActive = false
        private set
    var activationMove = 0L
        private set
    var duration = type.defaultDuration
        private set

    fun activate(currentMove: Long) {
        if (isActive || type == Type.NONE)
            return

        isActive = true
        activationMove = currentMove
    }

    fun turnsToExpiration(currentMove: Long): Long = duration - (currentMove - activationMove)

    fun stack() {
        if (type == Type.NONE)
            return

        duration += type.defaultDuration
    }

    enum class Type(private val text: String, val speedShift: Int, val defaultDuration: Long) {
        NONE("None", 0, 0),
        SLOW("[#00BFFF]Slow[]", -2, 100);

        override fun toString() = text
    }

    companion object {
        fun none() = PowerUp(Type.NONE, -5000f, -5000f, 0f, 0f)
    }
}