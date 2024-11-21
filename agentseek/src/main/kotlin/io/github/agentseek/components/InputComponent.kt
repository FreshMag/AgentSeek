package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.input.Input
import kotlin.time.Duration

class InputComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    companion object {
        const val STANDARD_VELOCITY = 3.5
    }

    override fun onUpdate(deltaTime: Duration) {
        val currentVelocity = Vector2d(
            if (Input.RIGHT) STANDARD_VELOCITY else if (Input.LEFT) -STANDARD_VELOCITY else 0.0,
            if (Input.UP) -STANDARD_VELOCITY else if (Input.DOWN) STANDARD_VELOCITY else 0.0
        )
        gameObject.rigidBody.velocity = if (Input.SHIFT) currentVelocity.div(2.0) else currentVelocity
    }
}
