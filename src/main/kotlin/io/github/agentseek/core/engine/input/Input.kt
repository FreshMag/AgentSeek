package io.github.agentseek.core.engine.input

import java.util.*

/**
 * Main object managing the input.
 */
object Input {

    /**
     * Containers of the aliases for the keys.
     */
    enum class Key {
        UP, DOWN, LEFT, RIGHT, SPACE,
    }

    private var inputProvider: InputProvider? = null
    private val inputBuffer: MutableSet<Key> = Collections.synchronizedSet(HashSet())

    val UP: Boolean
        get() = pressedKey(Key.UP)

    val DOWN: Boolean
        get() = pressedKey(Key.DOWN)

    val LEFT: Boolean
        get() = pressedKey(Key.LEFT)

    val RIGHT: Boolean
        get() = pressedKey(Key.RIGHT)

    val SPACE: Boolean
        get() = pressedKey(Key.SPACE)

    /**
     * Returns `true` if [key] has been pressed since last update
     */
    fun pressedKey(key: Key): Boolean = inputBuffer.contains(key)

    /**
     * Returns `true` if [key] has been pressed since last update or `false` either if it hasn't been pressed or
     * the input doesn't provide a mapping for that key
     */
    fun pressedKey(key: String): Boolean =
        try {
            inputBuffer.contains(Key.valueOf(key))
        } catch (e: IllegalArgumentException) {
            false
        }

    /**
     * Sets the provider for this input system.
     */
    fun injectProvider(inputProvider: InputProvider) {
        this.inputProvider = inputProvider
        inputProvider.addListener { inputBuffer.add(it) }
    }

    /**
     * Refreshes the input buffer, clearing all the previous input events.
     */
    internal fun refresh() {
        inputBuffer.clear()
    }
}
