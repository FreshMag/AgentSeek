package io.github.agentseek.util

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine.log
import kotlin.time.Duration

class DummyComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    override fun init() {
        gameObject.rigidBody.onCollision {
            println("I'm colliding!")
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        log("It's me the Dummy Component! Here's my ID: ${gameObject.id}")
        gameObject.rigidBody.applyForce(Vector2d(0.5, 0.25))
    }
}