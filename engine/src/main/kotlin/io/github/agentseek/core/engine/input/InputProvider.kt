package io.github.agentseek.core.engine.input

/**
 * Interface for the provider of input events. Can be a GUI or directly a class that "mocks" the input.
 */
interface InputProvider {
    /**
     * Adds a key listener for this provider
     */
    fun addKeyListener(listener: (Input.Key) -> Unit)

    /**
     * Adds a mouse listener for this provider
     */
    fun addMouseListener(listener: (Input.Mouse) -> Unit)
}