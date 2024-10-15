package io.github.agentseek.controller.events

import io.github.agentseek.controller.core.GameState

/**
 * This class manages a NewLevel Event and implements Event interface.
 */
class NewLevelEvent : Event {
    override fun handle(state: GameState) {
        state.generateNewLevel()
    }
}
