package io.github.agentseek.view.input

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.core.engine.input.InputProvider
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

open class InputListener : KeyListener, MouseListener, InputProvider {
    private var keyListeners = mutableListOf<(Input.Key) -> Unit>()
    private var mouseListeners = mutableListOf<(Input.Mouse) -> Unit>()

    override fun keyTyped(e: KeyEvent) {}

    override fun keyPressed(e: KeyEvent) {
        val inputKey: Input.Key? = when (e.keyCode) {
            KeyEvent.VK_W -> Input.Key.UP
            KeyEvent.VK_S -> Input.Key.DOWN
            KeyEvent.VK_D -> Input.Key.LEFT
            KeyEvent.VK_A -> Input.Key.RIGHT
            else -> null
        }
        inputKey?.let {
            keyListeners.forEach { listener -> listener(it) }
        }
    }

    override fun keyReleased(e: KeyEvent?) {}

    override fun addKeyListener(listener: (Input.Key) -> Unit) {
        keyListeners.add(listener)
    }

    override fun addMouseListener(listener: (Input.Mouse) -> Unit) {
        mouseListeners.add(listener)
    }

    override fun mouseClicked(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MouseClicked(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MousePressed(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        e?.let { event ->
            mouseListeners.forEach {
                it(Input.Mouse.MouseReleased(Point2d(event.x.toDouble(), event.y.toDouble())))
            }
        }
    }

    override fun mouseEntered(e: MouseEvent?) {}

    override fun mouseExited(e: MouseEvent?) {}
}