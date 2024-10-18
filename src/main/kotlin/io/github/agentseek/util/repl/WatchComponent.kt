package io.github.agentseek.util.repl

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import kotlin.time.Duration

internal class WatchComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        println(gameObject.toString())
    }
}