package io.github.agentseek.events

import io.github.agentseek.core.Game

/**
 * This class manages a GameOver Event and implements Event interface.
 */
class GameOverEvent : Event {
    override fun handle(state: Game) = state.callGameOver(false)

}
