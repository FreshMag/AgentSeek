package io.github.agentseek.core.engine

import io.github.agentseek.core.Game
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.time.Duration.Companion.milliseconds

/**
 * This is the main object that manages the game and the game loop.
 */
object GameEngine {

    private val logger = KotlinLogging.logger {}
    private val STANDARD_STARTING_PERIOD = 100.milliseconds
    private var state: Game? = null

    /**
     * The main loop of the game engine
     */
    private val loop: GameLoop by lazy {
        if (state == null) {
            loadScene(Game.emptyScene())
        }
        GameLoop(STANDARD_STARTING_PERIOD) { dt ->
            log("Test. Delta Time: $dt")
            state?.updateState(dt)
        }
    }

    fun loadScene(scene: Game) {
        state = scene
    }

    /**
     * Starts a non-blocking game loop
     */
    fun start() {
        loop.start()
    }

    /**
     * Pauses the game loop
     */
    fun pause() {
        loop.pause()
    }

    /**
     * Resumes the game loop
     */
    fun resume() {
        loop.resume()
    }

    /**
     * Stops the game loop
     */
    fun stop() {
        loop.stop()
    }

    /**
     * Logs a new [message]
     */
    fun log(message: String) {
        logger.debug { message }
    }

    /**
     * Does only one iteration of the game loop
     */
    fun doOne() {
        loop.doOne(STANDARD_STARTING_PERIOD)
    }

    /**
     * Stops the game loop and starts a new scene from scratch
     */
    fun reset() {
        stop()
        loadScene(Game.emptyScene())
    }
}
