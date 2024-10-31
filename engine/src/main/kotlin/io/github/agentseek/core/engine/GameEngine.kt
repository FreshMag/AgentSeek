package io.github.agentseek.core.engine

import io.github.agentseek.common.TimedAction
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.util.factories.SceneFactory
import io.github.agentseek.view.View
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.Collections
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * This is the main object that manages the game and the game loop.
 */
object GameEngine {

    private val logger = KotlinLogging.logger {}
    private val STANDARD_STARTING_PERIOD = 50.milliseconds
    private var scheduledActions: Map<String, TimedAction> = mapOf()

    private var scene: Scene? = null
    var view: View? = null

    /**
     * The main loop of the game engine
     */
    private val loop: GameLoop by lazy {
        if (scene == null) {
            loadScene(SceneFactory.emptyScene())
        }
        GameLoop(STANDARD_STARTING_PERIOD) { dt ->
            log("DT $dt")
            scene?.updateState(dt)
            Input.refresh()
            scheduledActions.values.forEach { it.applyIfElapsed() }
            view?.render()
        }
    }

    /**
     * Loads a scene into the engine
     */
    fun loadScene(scene: Scene) {
        this.scene = scene
    }

    /**
     * Schedules a new action to be performed every [period].
     */
    fun schedule(period: Duration, action: TimedAction.() -> Unit): String {
        val id = "scheduled@${scheduledActions.keys.size}"
        scheduledActions += id to TimedAction(id, period, action)
        return id
    }

    /**
     * Cancels a scheduled action.
     */
    fun cancel(id: String) {
        scheduledActions -= id
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

    /**
     * Gets a flag from the active scene, or null if that flag isn't set.
     */
    fun getFlag(id: String): Any? =
        scene?.flags?.get(id)
}
