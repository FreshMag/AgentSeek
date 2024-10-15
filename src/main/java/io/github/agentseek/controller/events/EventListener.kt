package io.github.agentseek.controller.events

/**
 * This interface manages Event instances.
 */
interface EventListener {
    /**
     * Notify the happening of an Event [e].
     */
    fun notifyEvent(e: Event)
}
