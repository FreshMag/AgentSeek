package io.github.agentseek.core.engine

import io.github.agentseek.core.GameState
import io.github.agentseek.events.EventHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds

/**
 * This is the main object that manages the game and the game loop.
 */
object GameEngine {

    private val logger = KotlinLogging.logger {}
    private val STANDARD_STARTING_PERIOD = 100.milliseconds
    private val state: GameState by lazy<GameState> { TODO() }
    val eventSystem: EventHandler by lazy { EventHandler(state) }

    /**
     * The main loop of the game engine
     */
    private val loop: GameLoop by lazy {
        GameLoop(STANDARD_STARTING_PERIOD) { dt ->
            log("Test. Delta Time: $dt")
            // state.updateState(dt)
            // eventSystem.handleEvents()
        }
    }

    /**
     * Initialize the game loop
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
     * Stops the game loop and the whole application
     */
    fun stop() {
        loop.stop()
        exitProcess(0)
    }

    /**
     * Logs a new [message]
     */
    fun log(message: String) {
        logger.debug { message }
    }
}
