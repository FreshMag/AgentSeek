package io.github.agentseek.events

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Game

/**
 * This class manages a RemoveEntity Event and implements Event interface.
 */
class RemoveEntityEvent(private var target: GameObject) : Event {

    override fun handle(state: Game) {
        this.removeEntity(target, state)
    }

    /**
     * Removes the [target] GameObject from the list of entities of the [state].
     */
    private fun removeEntity(target: GameObject, state: Game) {
        val removable = state.world.gameObjects.firstOrNull {
            it == target
        }
        removable?.delete()
    }
}

