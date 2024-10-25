package io.github.agentseek.core.engine.input

import io.github.agentseek.common.Point2d
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

    sealed class Mouse(val position: Point2d) {
        class MousePressed(position: Point2d) : Mouse(position)
        class MouseReleased(position: Point2d) : Mouse(position)
        class MouseClicked(position: Point2d) : Mouse(position)
    }

    private var inputProviders: List<InputProvider> = emptyList()
    private val keyInputBuffer: MutableSet<Key> = Collections.synchronizedSet(HashSet())
    private val mouseInputBuffer: MutableList<Mouse> = Collections.synchronizedList(mutableListOf())

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
     * Returns the position of the screen where the mouse was clicked, or null if the mouse was not clicked
     */
    fun mouseClicked(): Point2d? =
        mouseInputBuffer.lastOrNull { it is Mouse.MouseClicked }?.position

    /**
     * Returns the position of the screen where the mouse was pressed, or null if the mouse was not pressed
     */
    fun mousePressed(): Point2d? =
        mouseInputBuffer.lastOrNull { it is Mouse.MousePressed }?.position

    /**
     * Returns the position of the screen where the mouse was released, or null if the mouse was not released
     */
    fun mouseReleased(): Point2d? =
        mouseInputBuffer.lastOrNull { it is Mouse.MouseReleased }?.position

    /**
     * Returns `true` if [key] has been pressed since last update
     */
    fun pressedKey(key: Key): Boolean = keyInputBuffer.contains(key)

    /**
     * Returns `true` if [key] has been pressed since last update or `false` either if it hasn't been pressed or
     * the input doesn't provide a mapping for that key
     */
    fun pressedKey(key: String): Boolean = try {
        keyInputBuffer.contains(Key.valueOf(key))
    } catch (e: IllegalArgumentException) {
        false
    }

    /**
     * Sets the provider for this input system.
     */
    fun injectProvider(inputProvider: InputProvider) {
        this.inputProviders += inputProvider
        inputProvider.addKeyListener { keyInputBuffer.add(it) }
        inputProvider.addMouseListener { mouseInputBuffer.add(it) }
    }

    /**
     * Refreshes the input buffer, clearing all the previous input events.
     */
    internal fun refresh() {
        keyInputBuffer.clear()
        mouseInputBuffer.clear()
    }
}
