package io.github.agentseek.controller.events

import io.github.agentseek.controller.core.GameState
import io.github.agentseek.core.GameObject

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

