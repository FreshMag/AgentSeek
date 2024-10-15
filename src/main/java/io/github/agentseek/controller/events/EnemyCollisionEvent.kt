package ryleh.controller.events

import io.github.agentseek.controller.events.Event
import io.github.agentseek.controller.GameObject

/**
 * This class manages an EnemyCollision Event and implements Event interface.
 */
class EnemyCollisionEvent(target: GameObject) : Event {
    private val target: GameObject = target

    /**
     * {@inheritDoc} Decreases actual target health (if target is "PLAYER", health
     * will be decreased only in release mode).
     */
    override fun handle(state: GameState?) {
        if (target.getComponent(HealthIntComponent::class.java).isPresent()
            && !(target.type == Type.PLAYER && GameEngine.isDeveloper())
        ) {
            (target.getComponent(HealthIntComponent::class.java).get() as HealthIntComponent).damage(1)
        }
    }
}
