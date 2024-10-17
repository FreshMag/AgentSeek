package io.github.agentseek.core.engine.input

/**
 * Interface for the provider of input events. Can be a GUI or directly a class that "mocks" the input.
 */
interface InputProvider {
    /**
     * Adds a listener for this provider
     */
    fun addListener(listener: (Input.Key) -> Unit)
}