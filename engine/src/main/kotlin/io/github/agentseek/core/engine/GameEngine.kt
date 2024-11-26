package io.github.agentseek.core.engine

import io.github.agentseek.common.TimedAction
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine.init
import io.github.agentseek.core.engine.GameEngine.loop
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.events.Event
import io.github.agentseek.util.factories.SceneFactory
import io.github.agentseek.view.View
import io.github.oshai.kotlinlogging.KotlinLogging
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

    /**
     * The view that renders the game
     */
    var view: View? = null

    /**
     * Init function for the game engine. It gets called just before launching the main [loop].
     *
     * *Note*: is blocking
     */
    private val init = {
        if (scene == null) {
            loadScene(SceneFactory.emptyScene())
        }
        scene?.gameObjects?.forEach { it.components.forEach(Component::init) }
    }

    /**
     * The main loop of the game engine
     */
    private val loop: GameLoop by lazy {
        GameLoop(STANDARD_STARTING_PERIOD) { dt ->
            log("DT $dt")
            scene?.updateState(dt)
            Input.refresh()
            scheduledActions.values.forEach { it.applyIfElapsed() }
            view?.render()
        }
    }

    /**
     * Notifies an [Event] to the scene's world. Returns `false` if the event was not received.
     */
    fun notifySceneEvent(gameObject: GameObject, event: Event): Boolean =
        scene?.world?.let { it.notifyEvent(event, gameObject); true } == true

    /**
     * Loads a scene into the engine
     */
    fun loadScene(scene: Scene) {
        if (this.scene != null) {
            stop()
            destroyScene()
            this.scene = scene
            start()
        } else {
            this.scene = scene
        }
    }

    /**
     * Destroys the current scene
     */
    fun destroyScene() {
        this.scene?.gameObjects?.forEach { go -> go.delete() }
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
     * Starts a non-blocking game loop. This calls the [init] function before starting the game loop.
     *
     * *Note*: the init function is blocking.
     */
    fun start() {
        init()
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
