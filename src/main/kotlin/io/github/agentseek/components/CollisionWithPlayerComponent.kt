package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.events.EnemyCollisionEvent
import kotlin.time.Duration

/**
 * Component used to check when there's a collision with the player.
 */
class CollisionWithPlayerComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private var hasAlreadyCollided: Boolean = false

    /**
     * A method which checks for collisions with other [GameObject]s and notifies
     * world instance in case of event.
     */
    override fun onUpdate(deltaTime: Duration) {
        if (!this.hasAlreadyCollided) {
            val playerIfColliding: GameObject? = player?.takeIf { it.hitBox.isCollidingWith(gameObject.hitBox) }
            playerIfColliding?.let {
                gameObject.notifyEvent(EnemyCollisionEvent(it))
            }
        }
    }
}
