package io.github.agentseek.world

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.controller.core.GameEngine
import io.github.agentseek.controller.events.Event
import io.github.agentseek.controller.events.EventListener

/**
 * An implementation of Game World. Bounds are represented by a rectangle and game objects are contained inside a list.
 */
class WorldImpl(private val eventListener: EventListener) : World {
    override val gameObjects: MutableList<GameObject> = ArrayList()
    override val bounds: Rectangle2d =
        Rectangle2d(BOUNDS_WIDTH, BOUNDS_HEIGHT, BOUNDS_UPPER_LEFT_X, BOUNDS_UPPER_LEFT_Y)
    private var agentSeekId = 0

    /**
     * Instantiate a World and his bounds.
     */
    init {
        GameEngine.runDebugger { println("${bounds.upperLeft} ${bounds.upperRight}") }
    }

    override fun generateId(type: String): String {
        agentSeekId++
        return "${type}AG@${this.agentSeekId}"
    }

    override fun isOutOfBounds(position: Point2d): Boolean {
        return !bounds.contains(position)
    }

    override fun addGameObject(gameObject: GameObject) {
        gameObjects.add(gameObject)
        gameObject.onAdded(this)
    }

    override fun removeGameObject(gameObject: GameObject) {
        gameObjects.remove(gameObject)
    }

    override fun notifyWorldEvent(event: Event) {
        eventListener.notifyEvent(event)
    }

    companion object {
        private const val BOUNDS_WIDTH = 1407
        private const val BOUNDS_HEIGHT = 736
        private const val BOUNDS_UPPER_LEFT_X = 252
        private const val BOUNDS_UPPER_LEFT_Y = 172
    }
}