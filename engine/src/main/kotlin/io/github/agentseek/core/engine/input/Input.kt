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
        UP, DOWN, LEFT, RIGHT, SPACE, SHIFT
    }

    /**
     * Class that represents a mouse event.
     */
    sealed class Mouse(val position: Point2d) {
        class MousePressed(position: Point2d) : Mouse(position)
        class MouseReleased(position: Point2d) : Mouse(position)
        class MouseClicked(position: Point2d) : Mouse(position)
    }

    private var inputProviders: List<InputProvider> = emptyList()
    private val keyInputBuffer: MutableSet<Key> = Collections.synchronizedSet(HashSet())
    private val mouseInputBuffer: MutableList<Mouse> = Collections.synchronizedList(mutableListOf())

    /**
     * Returns `true` if the key UP(W) is pressed
     */
    val UP: Boolean
        get() = pressedKey(Key.UP)
    /**
     * Returns `true` if the key DOWN(S) is pressed
     */
    val DOWN: Boolean
        get() = pressedKey(Key.DOWN)
    /**
     * Returns `true` if the key LEFT(A) is pressed
     */
    val LEFT: Boolean
        get() = pressedKey(Key.LEFT)
    /**
     * Returns `true` if the key RIGHT(D) is pressed
     */
    val RIGHT: Boolean
        get() = pressedKey(Key.RIGHT)
    /**
     * Returns `true` if the key SPACE is pressed
     */
    val SPACE: Boolean
        get() = pressedKey(Key.SPACE)
    /**
     * Returns `true` if the key SHIFT is pressed
     */
    val SHIFT: Boolean
        get() = pressedKey(Key.SHIFT)

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
    } catch (_: IllegalArgumentException) {
        false
    }

    /**
     * Sets the provider for this input system.
     */
    fun injectProvider(inputProvider: InputProvider) {
        this.inputProviders += inputProvider
        inputProvider.addKeyListener({ keyInputBuffer.add(it) }, { keyInputBuffer.remove(it) })
        inputProvider.addMouseListener { mouseInputBuffer.add(it) }
    }

    /**
     * Refreshes the input buffer, clearing all the previous input events.
     */
    internal fun refresh() {
        mouseInputBuffer.clear()
    }
}
