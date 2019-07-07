package com.github.melodeiro.ksnake.logic

enum class Direction(private val axis: Axis) {
    UP(Axis.Y),
    DOWN(Axis.Y),
    LEFT(Axis.X),
    RIGHT(Axis.X);

    private enum class Axis { X, Y }

    infix fun isOppositeTo(to: Direction) = this.axis == to.axis
}