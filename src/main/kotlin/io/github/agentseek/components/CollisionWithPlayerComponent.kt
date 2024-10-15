package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Type
import io.github.agentseek.world.World
import ryleh.controller.events.EnemyCollisionEvent

/**
 * Component used to check when there's a collision with the player.
 */
class CollisionWithPlayerComponent(world: World, private val type: Type) : AbstractComponent(world) {
    private var hasAlreadyCollided: Boolean = false

    /**
     * A method which checks for collisions with other [GameObject]s and notifies
     * world instance in case of event.
     */
    override fun onUpdate(deltaTime: Double) {
        if (!this.hasAlreadyCollided) {
            val playerIfColliding: GameObject? = player?.takeIf { it.hitBox.isCollidingWith(gameObject.hitBox) }

            playerIfColliding?.let {
                if (type == Type.ITEM) {
                    world.notifyWorldEvent(ItemPickUpEvent())
                    this.hasAlreadyCollided = true
                } else {
                    world.notifyWorldEvent(EnemyCollisionEvent(it))
                }
            }
        }
    }
}
