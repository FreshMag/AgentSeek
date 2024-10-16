package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.events.EnemyCollisionEvent
import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * Component used to check when there's a collision with the player.
 */
class CollisionWithPlayerComponent(world: World) : AbstractComponent(world) {
    private var hasAlreadyCollided: Boolean = false

    /**
     * A method which checks for collisions with other [GameObject]s and notifies
     * world instance in case of event.
     */
    override fun onUpdate(deltaTime: Duration) {
        if (!this.hasAlreadyCollided) {
            val playerIfColliding: GameObject? = player?.takeIf { it.hitBox.isCollidingWith(gameObject.hitBox) }
            playerIfColliding?.let {
                world.notifyEvent(EnemyCollisionEvent(it))
            }
        }
    }
}
