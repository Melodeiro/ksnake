package com.github.melodeiro.ksnake.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.github.melodeiro.ksnake.App
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

class Game(val app: App) {
    var moves = 0
    var foodAteAmount = 0
    val snake = Array<Rectangle>()
    val difficulty = Difficulty()
    val foods = Array<Rectangle>()
    val traps = Array<Rectangle>()

    var isRunning: Boolean = false
        private set

    private var dX = 16f
    private var dY = 0f
    private var lastDirection = Direction.RIGHT
    private var newDirection = Direction.RIGHT
    private var isFoodEaten = false

    fun start() {
        spawnSnake()
        spawnRandomFood()
        respawnRandomTraps()
        isRunning = true

        KtxAsync.launch {
            while (isActive && isRunning) {
                delay(difficulty.speed)
                try {
                    val newFirstElementPoint = tryNextMove()
                    moves++
                    if (moves % difficulty.trapsRespawnEachTurn == 0)
                        respawnRandomTraps()
                    if (isFoodEaten) {
                        spawnSnakeElement(newFirstElementPoint.x, newFirstElementPoint.y, true)
                        foodAteAmount++
                        isFoodEaten = false
                        spawnRandomFood()
                        continue
                    }

                    for (i in snake.size - 1 downTo 1) {
                        snake[i].x = snake[i - 1].x
                        snake[i].y = snake[i - 1].y
                    }
                    snake[0].setPosition(newFirstElementPoint)
                } catch (e: GameOver) {
                    isRunning = false
                }
            }
        }
    }

    fun tryDirection(newDirection: Direction) {
        this.newDirection = newDirection
    }

    fun calculateScore(): Int {
        val tempScores = (snake.size - 4) * 100f * difficulty.speedLevel - moves
        return if (tempScores < 0) 0 else tempScores.toInt()
    }

    private fun spawnSnakeElement(x: Float, y: Float, firstElement: Boolean = false) {
        if (firstElement)
            snake.insert(0, Rectangle(x, y, 16f, 16f))
        else
            snake.add(Rectangle(x, y, 16f, 16f))
    }

    private fun spawnRandomFood() {
        var foodPos: Vector2
        do {
            foodPos = generateRandomPosition()
        } while (hasSomething(foodPos))
        foods.add(Rectangle(foodPos.x, foodPos.y, 16f, 16f))
    }

    private fun respawnRandomTraps() {
        traps.clear()
        repeat(difficulty.trapsToSpawn) {
            var trapPos: Vector2
            do {
                trapPos = generateRandomPosition()
            } while (hasSomething(trapPos))
            traps.add(Rectangle(trapPos.x, trapPos.y, 16f, 16f))
        }
    }

    private fun hasSomething(pos: Vector2): Boolean {
        foods.forEach {
            if (it.x == pos.x && it.y == pos.y)
                return true
        }
        traps.forEach {
            if (it.x == pos.x && it.y == pos.y)
                return true
        }
        snake.forEach {
            if (it.x == pos.x && it.y == pos.y)
                return true
        }
        return false
    }

    private fun generateRandomPosition() = Vector2(MathUtils.random(0, (app.cameraWidth / 16f).toInt() - 1) * 16f, MathUtils.random(0, (app.cameraHeight / 16f).toInt() - 1) * 16f)

    private fun spawnSnake() {
        spawnSnakeElement(96f, 64f) // last element
        spawnSnakeElement(80f, 64f)
        spawnSnakeElement(64f, 64f)
        spawnSnakeElement(48f, 64f) // first element
    }

    private fun tryNextMove(): Vector2 {
        val direction = if (newDirection isOppositeTo lastDirection) lastDirection else newDirection
        when (direction) {
            Direction.UP -> {
                dX = 0f
                dY = 16f
            }
            Direction.DOWN -> {
                dX = 0f
                dY = -16f
            }
            Direction.LEFT -> {
                dX = -16f
                dY = 0f
            }
            Direction.RIGHT -> {
                dX = 16f
                dY = 0f
            }
        }
        lastDirection = direction
        var tempX = snake[0].x + dX
        var tempY = snake[0].y + dY
        if (tempX > app.cameraWidth - 16f)
            tempX = 0f
        if (tempY > app.cameraHeight - 16f)
            tempY = 0f
        if (tempX < 0)
            tempX = app.cameraWidth - 16f
        if (tempY < 0)
            tempY = app.cameraHeight - 16f
        val newPos = Vector2(tempX, tempY)
        val lastElement = snake.last()
        snake.forEach {
            if (it.x == newPos.x && it.y == newPos.y && it != lastElement)
                throw GameOver()
        }
        traps.forEach {
            if (it.x == newPos.x && it.y == newPos.y) {
                traps.removeValue(it, true)
                ateBadFood()
            }
        }
        foods.forEach {
            if (it.x == newPos.x && it.y == newPos.y) {
                foods.removeValue(it, true)
                isFoodEaten = true
            }
        }
        return newPos
    }

    private fun ateBadFood() {
        snake.removeIndex(snake.size - 1)
        if (snake.isEmpty)
            throw GameOver()
    }
}