package io.github.agentseek.core.engine

import io.github.agentseek.core.Game
import io.github.agentseek.util.factories.SceneFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.time.Duration
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
            loadScene(SceneFactory.emptyScene())
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
     * Logs a new [message] as an error.
     */
    fun logError(message: String) {
        logger.error { message }
    }

    /**
     * Does only one iteration of the game loop
     */
    fun doOne(artificialDeltaTime: Duration = STANDARD_STARTING_PERIOD) {
        loop.doOne(artificialDeltaTime)
    }

    /**
     * Stops the game loop and starts a new scene from scratch
     */
    fun reset() {
        stop()
        loadScene(SceneFactory.emptyScene())
    }
}
