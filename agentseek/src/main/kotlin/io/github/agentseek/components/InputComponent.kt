package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.input.Input
import kotlin.time.Duration

class InputComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private val standardVelocity = Config.Player.speed

    override fun onUpdate(deltaTime: Duration) {
        val currentVelocity = Vector2d(
            if (Input.RIGHT) standardVelocity else if (Input.LEFT) -standardVelocity else 0.0,
            if (Input.UP) -standardVelocity else if (Input.DOWN) standardVelocity else 0.0
        )
        gameObject.rigidBody.velocity = if (Input.SHIFT) currentVelocity.div(2.0) else currentVelocity
    }
}
