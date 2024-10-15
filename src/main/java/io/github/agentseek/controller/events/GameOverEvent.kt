package io.github.agentseek.controller.events

import ryleh.controller.core.GameState

/**
 * This class manages a GameOver Event and implements Event interface.
 */
class GameOverEvent : Event {
    /**
     * {@inheritDoc} Sets the game over view
     */
    override fun handle(state: GameState) {
        state.callGameOver(false)
    }
}
