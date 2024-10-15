package io.github.agentseek.model.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.controller.GameObject
import io.github.agentseek.controller.Type
import io.github.agentseek.model.World

/**
 * A class that provides the Component for lurker enemy type.
 */
class LurkerComponent(world: World) : AbstractComponent(world) {
    private var adjustDirectionTimer = System.currentTimeMillis()
    private var velocity: Vector2d = Vector2d.zero()

    private val directionToPlayer: Vector2d
        get() = Vector2d(player?.position ?: Point2d.origin(), gameObject.position)

    override fun onAdded(gameObject: GameObject) {
        adjustVelocity()
        super.onAdded(gameObject)
    }

    override fun onUpdate(deltaTime: Double) {
        move()
        this.checkScreenBounds()
    }

    /**
     * Generates new position to move to.
     */
    private fun move() {
        if (System.currentTimeMillis() - adjustDirectionTimer >= ADJUST_DELAY) {
            adjustVelocity()
            adjustDirectionTimer = System.currentTimeMillis()
        }
        gameObject.position += velocity
    }

    /**
     * Checks game world bounds or collision with a rock and if true bounces back.
     */
    private fun checkScreenBounds() {
        if (gameObject.hitBox.isOutOfBounds(world.bounds) || this.isCollidingWithRock) {
            velocity.x = -velocity.x
            velocity.y = -velocity.y
        }
    }

    /**
     * Adjusts velocity vector to follow player direction.
     */
    private fun adjustVelocity() {
        velocity += (directionToPlayer.normalized() * MOVE_SPEED) * TPF
    }

    private val isCollidingWithRock: Boolean
        /**
         * Checks if the GameObject collided with a rock.
         */
        get() =
            world.gameObjects
                .filter { it.type == Type.ROCK }
                .any { it.hitBox.isCollidingWith(gameObject.hitBox) }


    companion object {
        private const val ADJUST_DELAY: Long = 250
        private const val MOVE_SPEED = 50.0
        private const val TPF = 0.016
    }
}
