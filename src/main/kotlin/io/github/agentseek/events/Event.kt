package io.github.agentseek.events

import io.github.agentseek.core.GameState


/**
 * This interface represents an Event and the action performed by one.
 */
interface Event {
    /**
     * Perform some actions on the [state] depending on the specified event.
     */
    fun handle(state: GameState)
}
