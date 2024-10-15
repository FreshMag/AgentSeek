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
     * Removes the target from the list of entities.
     *
     * @param target Game object to be removed
     * @param state  The actual state of the game
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

