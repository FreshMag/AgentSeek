package io.github.agentseek.util

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine.log
import kotlin.time.Duration

class DummyComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    override fun onUpdate(deltaTime: Duration) {
        log("It's me the Dummy Component! Here's my ID: ${gameObject.id}")
    }
}