package io.github.agentseek.view.input

import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.core.engine.input.InputProvider
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

open class InputListener : KeyListener, InputProvider {
    private var inputListeners = mutableListOf<(Input.Key) -> Unit>()
    override fun keyTyped(e: KeyEvent) {
    }

    override fun keyPressed(e: KeyEvent) {
        val inputKey: Input.Key? = when (e.keyCode) {
            KeyEvent.VK_W -> Input.Key.UP
            KeyEvent.VK_S -> Input.Key.DOWN
            KeyEvent.VK_D -> Input.Key.LEFT
            KeyEvent.VK_A -> Input.Key.RIGHT
            else -> null
        }
        inputKey?.let {
            inputListeners.forEach { listener -> listener(it) }
        }
    }

    override fun keyReleased(e: KeyEvent?) {
    }

    override fun addListener(listener: (Input.Key) -> Unit) {
        inputListeners.add(listener)
    }
}