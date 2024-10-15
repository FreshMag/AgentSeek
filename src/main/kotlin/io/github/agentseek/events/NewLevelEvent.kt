package io.github.agentseek.events

import io.github.agentseek.core.GameState

/**
 * This class manages a NewLevel Event and implements Event interface.
 */
class NewLevelEvent : Event {
    override fun handle(state: GameState) {
        state.generateNewLevel()
    }
}
