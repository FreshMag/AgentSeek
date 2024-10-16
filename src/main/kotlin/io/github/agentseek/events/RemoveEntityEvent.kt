package io.github.agentseek.events

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.GameState

/**
 * This class manages a RemoveEntity Event and implements Event interface.
 */
class RemoveEntityEvent(private var target: GameObject) : Event {

    override fun handle(state: GameState) {
        this.removeEntity(target, state)
    }

    /**
     * Removes the [target] GameObject from the list of entities of the [state].
     */
    private fun removeEntity(target: GameObject, state: GameState) {
        val removable = state.entities.firstOrNull {
            it == target
        }
        removable?.let {
            state.removeGameObject(it)
        }
    }
}

