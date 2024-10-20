package io.github.agentseek.events

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Game

/**
 * This class manages an EnemyCollision Event and implements Event interface.
 */
class EnemyCollisionEvent(private val target: GameObject) : Event {
    override fun handle(state: Game) {
        TODO()
    }
}
