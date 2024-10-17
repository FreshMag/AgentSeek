package io.github.agentseek.events

import io.github.agentseek.core.Game


/**
 * This interface represents an Event and the action performed by one.
 */
fun interface Event {
    /**
     * Perform some actions on the [state] depending on the specified event.
     */
    fun handle(state: Game)
}
