package com.github.melodeiro.ksnake.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Queue
import com.github.melodeiro.ksnake.App
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

class Game(private val app: App) {
    val difficulty = Difficulty()

    val snake = Array<Rectangle>()
    val foods = Array<Rectangle>()
    val traps = Array<Rectangle>()

    var isRunning: Boolean = false
        private set

    private val controlQueue = Queue<Direction>().apply { addFirst(Direction.RIGHT) }

    private var moves = 0
    private var foodAteAmount = 0
    private var dX = 16f
    private var dY = 0f
    private var lastDirection = Direction.RIGHT
    private var isFoodEaten = false
    private var isBadFoodEaten = false
    private var controlsResetIn = 0

    fun start() {
        spawnSnake()
        spawnRandomFood()
        repeat(difficulty.trapsToSpawn) { spawnRandomTrap() }
        isRunning = true

        KtxAsync.launch {
            while (isActive && isRunning) {
                delay(difficulty.speed)
                try {
                    val newFirstElementPoint = tryNextMove()
                    if (isBadFoodEaten) {
                        spawnRandomTrap()
                        isBadFoodEaten = false
                    }
                    moves++
                    if (moves % difficulty.turnsToNewTrap == 0)
                        spawnRandomTrap(true)
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
        if (!controlQueue.isEmpty && controlQueue.last() == newDirection)
            return

        controlQueue.addLast(newDirection)
        controlsResetIn = 2
    }

    fun calculateScore(): Int {
        val tempScores = (snake.size - 4) * 100f * difficulty.speedLevel - moves
        return if (tempScores < 0) 0 else tempScores.toInt()
    }

    private fun spawnSnakeElement(x: Float, y: Float, firstElement: Boolean = false) {
        val newElement = Rectangle(x, y, 16f, 16f)
        if (firstElement)
            snake.insert(0, newElement)
        else
            snake.add(newElement)
    }

    private fun spawnRandomFood() {
        var foodPos: Vector2
        do {
            foodPos = getRandomPosition()
        } while (hasSomething(foodPos))
        foods.add(Rectangle(foodPos.x, foodPos.y, 16f, 16f))
    }

    private fun spawnRandomTrap(removeLast: Boolean = false) {
        if (removeLast)
            traps.removeIndex(traps.size - 1)

        var trapPos: Vector2
        do {
            trapPos = getRandomPosition()
        } while (hasSomething(trapPos))
        val trapRect = Rectangle(trapPos.x, trapPos.y, 16f, 16f)

        if (removeLast)
            traps.insert(0, trapRect)
        else
            traps.add(trapRect)
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

    private fun getRandomPosition() = Vector2(MathUtils.random(0, (app.cameraWidth / 16f).toInt() - 1) * 16f, MathUtils.random(0, (app.cameraHeight / 16f).toInt() - 1) * 16f)

    private fun spawnSnake() {
        spawnSnakeElement(96f, 64f) // last element
        spawnSnakeElement(80f, 64f)
        spawnSnakeElement(64f, 64f)
        spawnSnakeElement(48f, 64f) // first element
    }

    private fun tryNextMove(): Vector2 {
        if (controlsResetIn < 1)
            controlQueue.clear()
        else
            controlsResetIn--
        val newDirection: Direction? = if (controlQueue.isEmpty) null else controlQueue.removeFirst()
        val direction = if (newDirection == null || newDirection isOppositeTo lastDirection) lastDirection else newDirection
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
                isBadFoodEaten = true
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

    fun restart() {
        moves = 0
        foodAteAmount = 0
        dX = 16f
        dY = 0f
        lastDirection = Direction.RIGHT
        controlQueue.clear()
        controlQueue.addFirst(Direction.RIGHT)
        isFoodEaten = false
        isBadFoodEaten = false
        controlsResetIn = 0

        snake.clear()
        foods.clear()
        traps.clear()
    }
}