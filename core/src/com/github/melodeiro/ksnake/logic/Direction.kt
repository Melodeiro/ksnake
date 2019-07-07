package com.github.melodeiro.ksnake.logic

private enum class Axis { X, Y }

enum class Direction(private val axis: Axis) {
    UP(Axis.Y),
    DOWN(Axis.Y),
    LEFT(Axis.X),
    RIGHT(Axis.X);

    infix fun isOppositeTo(to: Direction) = this.axis == to.axis
}