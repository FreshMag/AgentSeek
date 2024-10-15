package ryleh.controller.events

import io.github.agentseek.controller.core.GameState
import io.github.agentseek.controller.events.Event
import io.github.agentseek.core.GameObject

/**
 * This class manages an EnemyCollision Event and implements Event interface.
 */
class EnemyCollisionEvent(private val target: GameObject) : Event {
    override fun handle(state: GameState) {
        TODO()
    }
}
