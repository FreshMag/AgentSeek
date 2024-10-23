package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.input.Input
import kotlin.time.Duration

class InputComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        super.onUpdate(deltaTime)
        when {
            Input.UP -> gameObject.rigidBody.applyForce(Vector2d(0.0, -20.0))
            Input.DOWN -> gameObject.rigidBody.applyForce(Vector2d(0.0, 20.0))
            Input.RIGHT -> gameObject.rigidBody.applyForce(Vector2d(-20.0, 0.0))
            Input.LEFT -> gameObject.rigidBody.applyForce(Vector2d(20.0, 0.0))
            else -> gameObject.rigidBody.velocity = Vector2d(0.0, 0.0)
        }
    }
}