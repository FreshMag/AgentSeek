package io.github.agentseek.view.input

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.core.engine.input.InputProvider
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

/**
 * Class that handles keyboard and mouse input events.
 */
open class InputListener : KeyListener, MouseListener, InputProvider {
    private var keyListeners = mutableListOf<Pair<(Input.Key) -> Unit, (Input.Key) -> Unit>>()
    private var mouseListeners = mutableListOf<(Input.Mouse) -> Unit>()
    private var pressedKeySet = mutableSetOf<Int>()

    /**
     * Method called when a key is typed.
     * @param e Key event.
     */
    override fun keyTyped(e: KeyEvent) {}

    /**
     * Method called when a key is pressed.
     * @param e Key event.
     */
    override fun keyPressed(e: KeyEvent) {
        val inputKey = getMappedInput(e)
        inputKey?.let {
            pressedKeySet.add(e.keyCode)
            keyListeners.forEach { listener -> listener.first(it) }
        }
    }

    /**
     * Method called when a key is released.
     * @param e Key event.
     */
    override fun keyReleased(e: KeyEvent) {
        pressedKeySet.remove(e.keyCode)
        val inputKey = getMappedInput(e)
        inputKey?.let {
            keyListeners.forEach { listener ->
                listener.second(it)
            }
        }
    }

    /**
     * Adds a listener for key press and release events.
     * @param keyPressedListener Function called when a key is pressed.
     * @param keyReleasedListener Function called when a key is released.
     */
    override fun addKeyListener(keyPressedListener: (Input.Key) -> Unit, keyReleasedListener: (Input.Key) -> Unit) {
        keyListeners.add(Pair(keyPressedListener, keyReleasedListener))
    }

    /**
     * Adds a listener for mouse events.
     * @param listener Function called when a mouse event occurs.
     */
    override fun addMouseListener(listener: (Input.Mouse) -> Unit) {
        mouseListeners.add(listener)
    }

    /**
     * Method called when the mouse is clicked.
     * @param e Mouse event.
     */
    override fun mouseClicked(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MouseClicked(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    /**
     * Method called when a mouse button is pressed.
     * @param e Mouse event.
     */
    override fun mousePressed(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MousePressed(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    /**
     * Method called when a mouse button is released.
     * @param e Mouse event.
     */
    override fun mouseReleased(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MouseReleased(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    /**
     * Method called when the mouse enters a component.
     * @param e Mouse event.
     */
    override fun mouseEntered(e: MouseEvent?) {}

    /**
     * Method called when the mouse exits a component.
     * @param e Mouse event.
     */
    override fun mouseExited(e: MouseEvent?) {}

    /**
     * Maps a key event to a specific input.
     * @param e Key event.
     * @return The mapped input or null if not mapped.
     */
    private fun getMappedInput(e: KeyEvent) = when (e.keyCode) {
        KeyEvent.VK_W -> Input.Key.UP
        KeyEvent.VK_S -> Input.Key.DOWN
        KeyEvent.VK_D -> Input.Key.RIGHT
        KeyEvent.VK_A -> Input.Key.LEFT
        KeyEvent.VK_SHIFT -> Input.Key.SHIFT
        else -> null
    }
}