package io.github.agentseek.model

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.controller.events.Event

/**
 * Game's world. The World represents the domain of the game, containing all game objects and the bounds where the
 * objects can move.
 */
interface World {
    /**
     * Gets the list of game objects added to this world.
     */
    val gameObjects: List<GameObject>

    /**
     * Generate a basic unique ID for each object that has a reference to this world, using the [type] of the object
     * added.
     */
    fun generateId(type: String): String

    /**
     * Checks if a certain [position] is out of bounds.
     * `true` if that position is out of bounds.
     */
    fun isOutOfBounds(position: Point2d): Boolean

    /**
     * Add an [gameObject] to this world.
     */
    fun addGameObject(gameObject: GameObject)

    /**
     * Two-dimensional shape that represents world [bounds].
     */
    val bounds: Shape2d

    /**
     * Remove a [gameObject] from this world.
     */
    fun removeGameObject(gameObject: GameObject)

    /**
     * Notify an [event] to the EventListener.
     */
    fun notifyWorldEvent(event: Event)
}
