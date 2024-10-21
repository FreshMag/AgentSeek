package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import kotlin.time.Duration

class ConstantAccelerationComponent(
    gameObject: GameObject,
    private val acceleration: Vector2d,
) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        gameObject.rigidBody.applyForce(acceleration)
    }
}