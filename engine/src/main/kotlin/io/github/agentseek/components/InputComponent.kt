package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.input.Input
import kotlin.time.Duration

class InputComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        gameObject.rigidBody.velocity = Vector2d(
            if (Input.RIGHT) 2.0 else if (Input.LEFT) -2.0 else 0.0,
            if (Input.UP) -2.0 else if (Input.DOWN) 2.0 else 0.0
        )
    }
}
