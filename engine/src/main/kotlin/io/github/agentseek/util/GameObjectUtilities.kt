package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject

object GameObjectUtilities {

    /**
     * Returns all the other game objects except for this one
     */
    fun GameObject.otherGameObjects(): Iterable<GameObject> =
        world.gameObjects.filterNot { it.id == this.id }

    /**
     * Returns the center of the rigid body of this object
     */
    fun GameObject.center(): Point2d =
        rigidBody.shape.center

}
