package io.github.agentseek.util

import io.github.agentseek.core.GameObject

object GameObjectUtilities {

    fun GameObject.otherGameObjects(): Iterable<GameObject> =
        world.gameObjects.filterNot { it.id == this.id }
}