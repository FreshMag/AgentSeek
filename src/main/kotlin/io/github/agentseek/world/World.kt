package io.github.agentseek.world

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.Event
import io.github.agentseek.util.GameObjectBuilder

/**
 * Game's world. The World represents the domain of the game, containing all game objects and the bounds where the
 * objects can move.
 */
interface World {
    /**
     * Gets the list of game objects added to this world.
     */
    val gameObjects: Iterable<GameObject>

    /**
     * Generate a basic unique ID for each object that has a reference to this world, using the [type] of the object
     * added.
     */
    fun generateId(type: String): String

    /**
     * Gets a [GameObject] by id, or `null` if that object doesn't exist.
     */
    fun gameObjectById(id: String): GameObject?

    /**
     * Add an [gameObject] to this world.
     */
    fun addGameObject(gameObject: GameObject)

    /**
     * Remove a [gameObject] from this world.
     */
    fun removeGameObject(gameObject: GameObject)

    /**
     * Notify an [event] to the EventListener.
     */
    fun notifyEvent(event: Event, source: GameObject)

    /**
     * Instantiates a new [GameObject] builder related to this world
     */
    fun gameObjectBuilder(): GameObjectBuilder = GameObjectBuilder(this)

}
