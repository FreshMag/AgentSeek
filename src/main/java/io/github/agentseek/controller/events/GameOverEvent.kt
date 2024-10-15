package io.github.agentseek.controller.events

import io.github.agentseek.controller.core.GameState

/**
 * This class manages a GameOver Event and implements Event interface.
 */
class GameOverEvent : Event {
    override fun handle(state: GameState) = state.callGameOver(false)

}
