package io.github.agentseek.controller.events

import ryleh.controller.core.GameState

/**
 * This class manages a NewLevel Event and implements Event interface.
 */
class NewLevelEvent : Event {
    /**
     * {@inheritDoc} Generate a new random level
     */
    override fun handle(state: GameState) {
        state.generateNewLevel()
    }
}
