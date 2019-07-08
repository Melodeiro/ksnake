package com.github.melodeiro.ksnake.logic

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ArrayMap
import com.badlogic.gdx.utils.Queue
import com.github.melodeiro.ksnake.App
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.async.KtxAsync

class Game(private val app: App) {
    val field = WorldBorders(160f, 64f, 480f, 480f)
    val difficulty = Difficulty()

    val snake = Array<Rectangle>()
    val foods = Array<Rectangle>()
    val traps = Array<Rectangle>()

    var isRunning: Boolean = false
        private set

    private val controlQueue = Queue<Direction>().apply { addFirst(Direction.RIGHT) }

    private val cellSize = 16f
    private var moves = 0
    private var foodAteAmount = 0
    private var dX = cellSize
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

                    // move snake from last to second element
                    for (i in snake.size - 1 downTo 1) {
                        snake[i].x = snake[i - 1].x
                        snake[i].y = snake[i - 1].y
                    }
                    // move snake head
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
        val newElement = Rectangle(x, y, cellSize, cellSize)
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
        foods.add(Rectangle(foodPos.x, foodPos.y, cellSize, cellSize))
    }

    private fun spawnRandomTrap(removeLast: Boolean = false) {
        if (removeLast)
            traps.removeIndex(traps.size - 1)

        var trapPos: Vector2
        do {
            trapPos = getRandomPosition()
        } while (hasSomething(trapPos))
        val trapRect = Rectangle(trapPos.x, trapPos.y, cellSize, cellSize)

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

    private fun getRandomCellPos(start: Float, end: Float): Float = MathUtils.random((start / cellSize).toInt(), (end / cellSize).toInt()) * cellSize

    private fun getRandomPosition() = Vector2(getRandomCellPos(field.x, field.maxX - cellSize), getRandomCellPos(field.y, field.maxY - cellSize))

    private fun spawnSnake() {
        spawnSnakeElement(field.x + 10f * cellSize, field.y + 7f * cellSize) // last element
        spawnSnakeElement(field.x + 9f * cellSize, field.y + 7f * cellSize)
        spawnSnakeElement(field.x + 8f * cellSize, field.y + 7f * cellSize)
        spawnSnakeElement(field.x + 7f * cellSize, field.y + 7f * cellSize) // first element
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
                dY = cellSize
            }
            Direction.DOWN -> {
                dX = 0f
                dY = -cellSize
            }
            Direction.LEFT -> {
                dX = -cellSize
                dY = 0f
            }
            Direction.RIGHT -> {
                dX = cellSize
                dY = 0f
            }
        }
        lastDirection = direction
        var tempX = snake[0].x + dX
        var tempY = snake[0].y + dY
        if (tempX > field.maxX - cellSize)
            tempX = field.x
        if (tempY > field.maxY - cellSize)
            tempY = field.y
        if (tempX < field.x)
            tempX = field.maxX - cellSize
        if (tempY < field.y)
            tempY = field.maxY - cellSize
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
        dX = cellSize
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