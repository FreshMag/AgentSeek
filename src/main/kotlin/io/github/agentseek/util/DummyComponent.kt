package io.github.agentseek.util

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine.log
import kotlin.time.Duration
import kotlin.time.DurationUnit

class DummyComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    override fun onUpdate(deltaTime: Duration) {
        log("It's me the Dummy Component! Here's my ID: ${gameObject.id}")
        gameObject.position += Vector2d(1.0, 1.0).normalized() *
                (deltaTime.toDouble(DurationUnit.MILLISECONDS) / 10)
    }
}