package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject

object GameObjectUtilities {

    fun GameObject.otherGameObjects(): Iterable<GameObject> =
        world.gameObjects.filterNot { it.id == this.id }

    fun GameObject.center(): Point2d {
        return rigidBody.shape.center

    }
}