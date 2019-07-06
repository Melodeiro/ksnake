package com.github.melodeiro.ksnake.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.github.melodeiro.ksnake.Game
import com.github.melodeiro.ksnake.game.GameOver
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class GameScreen(private val game: Game) : KtxScreen {
    private enum class Direction(val axis: Axis) { UP(Axis.Y), DOWN(Axis.Y), LEFT(Axis.X), RIGHT(Axis.X) }
    private enum class Axis { X, Y }

    // The camera ensures we can render using our target resolution of 800x480
    //    pixels no matter what the screen resolution is.
    private val camera = OrthographicCamera()
            .apply { setToOrtho(false, game.cameraWidth, game.cameraHeight) }

    private val snakeElementImage = Texture("snake_element.png")
    private val foodImage = Texture("food.png")
    private val trapImage = Texture("trap.png")
    private val snake = Array<Rectangle>()
    private var dX = 16f
    private var dY = 0f
    private var lastDirection = Direction.RIGHT
    private var newDirection = Direction.RIGHT
    private var food = Rectangle()
    private val traps = Array<Rectangle>()
    private var isFoodEaten = false

    private fun spawnSnakeElement(x: Float, y: Float, firstElement: Boolean = false) {
        if (firstElement)
            snake.insert(0, Rectangle(x, y, 16f, 16f))
        else
            snake.add(Rectangle(x, y, 16f, 16f))
    }

    private fun generateRandomPosition() = Vector2(MathUtils.random(0, (game.cameraWidth / 16f).toInt() - 1) * 16f, MathUtils.random(0, (game.cameraHeight / 16f).toInt() - 1) * 16f)

    private fun spawnRandomFood() {
        var foodPos: Vector2
        do {
            foodPos = generateRandomPosition()
        } while (hasSomething(foodPos))

        food = Rectangle(foodPos.x, foodPos.y, 16f, 16f)
    }

    private fun spawnRandomTraps(amount: Int) {
        traps.clear()
        repeat(amount) {
            var trapPos: Vector2
            do {
                trapPos = generateRandomPosition()
            } while (hasSomething(trapPos))
            // TODO() prevent spawn close to snake
            traps.add(Rectangle(trapPos.x, trapPos.y, 16f, 16f))
        }
    }

    override fun render(delta: Float) {
        // generally good practice to update the camera's matrices once per frame
        camera.update()
        game.batch.projectionMatrix = camera.combined

        // Draw all textures
        game.batch.begin()
        snake.forEach { game.batch.draw(snakeElementImage, it.x, it.y, it.width, it.height) }
        traps.forEach { game.batch.draw(trapImage, it.x, it.y, it.width, it.height) }
        game.batch.draw(foodImage, food.x, food.y, food.width, food.height)
        game.batch.end()

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            newDirection = Direction.LEFT
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            newDirection = Direction.RIGHT
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            newDirection = Direction.UP
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            newDirection = Direction.DOWN
        }
    }

    override fun show() {
        spawnSnakeElement(96f, 64f) // last element
        spawnSnakeElement(80f, 64f)
        spawnSnakeElement(64f, 64f)
        spawnSnakeElement(48f, 64f) // first element
        spawnRandomFood()
        spawnRandomTraps(game.difficulty.trapsToSpawn)

        KtxAsync.launch {
            while (isActive) {
                delay(game.difficulty.speed)
                try {
                    val newFirstElementPoint = tryNextMove()
                    game.moves++
                    if (game.moves % game.difficulty.trapsRespawnEachTurn == 0)
                        spawnRandomTraps(game.difficulty.trapsToSpawn)
                    if (isFoodEaten) {
                        spawnSnakeElement(newFirstElementPoint.x, newFirstElementPoint.y, true)
                        game.foodAteAmount++
                        isFoodEaten = false
                        continue
                    }

                    for (i in snake.size - 1 downTo 1) {
                        snake[i].x = snake[i - 1].x
                        snake[i].y = snake[i - 1].y
                    }
                    snake[0].setPosition(newFirstElementPoint)
                } catch (e: GameOver) {
                    println("gamescreen bug")
                    game.addScreen(GameOverScreen(game))
                    game.setScreen<GameOverScreen>()
                    game.removeScreen<GameScreen>()
                    dispose()
                    throw CancellationException("Game Over")
                }
            }
        }
    }

    private fun tryNextMove(): Vector2 {
        val direction = if (newDirection.axis == lastDirection.axis && newDirection != lastDirection) lastDirection else newDirection
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
        if (tempX > game.cameraWidth - 16f)
            tempX = 0f
        if (tempY > game.cameraHeight - 16f)
            tempY = 0f
        if (tempX < 0)
            tempX = game.cameraWidth - 16f
        if (tempY < 0)
            tempY = game.cameraHeight - 16f
        val newPos = Vector2(tempX, tempY)
        val lastElement = snake.last()
        snake.forEach {
            if (it.x == newPos.x && it.y == newPos.y && it != lastElement)
                throw GameOver()
        }
        traps.forEach {
            if (it.x == newPos.x && it.y == newPos.y && it != lastElement)
                throw GameOver()
        }
        if (food.x == newPos.x && food.y == newPos.y) {
            spawnRandomFood()
            isFoodEaten = true
        }
        return newPos
    }

    private fun hasSomething(pos: Vector2): Boolean {
        if (pos.x == food.x && pos.y == food.y)
            return true
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
}