package io.github.agentseek.world

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine.log
import io.github.agentseek.events.Event
import io.github.agentseek.events.EventListener

/**
 * An implementation of Game World. Bounds are represented by a rectangle and game objects are contained inside a list.
 */
class WorldImpl(private val eventListener: EventListener) : World {
    override var gameObjects: List<GameObject> = emptyList()
        private set
    private var agentSeekId = 0


    override fun generateId(type: String): String {
        agentSeekId++
        return "${type}AG@${this.agentSeekId}"
    }

    override fun gameObjectById(id: String): GameObject? =
        gameObjects.firstOrNull { it.id == id }


    override fun addGameObject(gameObject: GameObject) {
        try {
            gameObjects += gameObject
        } catch (e: Exception) {
            log(e.message ?: "There was an exception while adding GameObject")
        }
    }

    override fun removeGameObject(gameObject: GameObject) {
        try {
            gameObjects -= gameObject
        } catch (e: Exception) {
            log(e.message ?: "There was an exception while removing GameObject")
        }
    }

    override fun notifyEvent(event: Event, source: GameObject) {
        log("Notified event $event from $source")
        eventListener.notifyEvent(event)
    }
}
