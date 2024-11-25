package io.github.agentseek.util.repl

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import kotlin.time.Duration

/**
 * A component that watches the game object it is attached to.
 */
internal class WatchComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        println(gameObject.toString())
    }
}