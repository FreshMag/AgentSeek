package io.github.agentseek.events

import io.github.agentseek.core.GameState


/**
 * This interface represents an Event and the action performed by one.
 */
interface Event {
    /**
     * Perform some actions depending on the specified event.
     *
     * @param state is the current state of the game.
     */
    fun handle(state: GameState)
}
