package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.input.Input
import kotlin.system.exitProcess
import kotlin.time.Duration

/**
 * A component that exits the game when the user clicks the mouse.
 */
class ClickToExitComponent(gameObject: GameObject): AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        if (Input.mouseClicked() != null) {
            exitProcess(0)
        }
    }
}