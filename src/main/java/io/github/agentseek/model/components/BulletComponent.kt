package io.github.agentseek.model.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.controller.GameObject
import io.github.agentseek.controller.Type.*
import io.github.agentseek.model.World
import ryleh.controller.events.EnemyCollisionEvent
import io.github.agentseek.controller.events.RemoveEntityEvent

/**
 * Component that describes bullet's behavior in the game.
 */
class BulletComponent(world: World, private val origin: Point2d, direction: Vector2d) : AbstractComponent(world) {

    private val velocity: Vector2d = direction.multiply(SPEED.toDouble())

    override fun onAdded(gameObject: GameObject) {
        super.onAdded(gameObject)
        gameObject.position = origin
    }

    override fun onUpdate(deltaTime: Double) {
        move(deltaTime)
        checkCollision()
    }

    private fun checkCollision() {
        val colliding: GameObject? = if (gameObject.type != PLAYER_BULLET) {
            checkPlayerCollision()
        } else {
            checkEnemyCollision()
        }
        colliding?.let {
            world.notifyWorldEvent(EnemyCollisionEvent(it))
            if (gameObject.hitBox.isOutOfBounds(world.bounds)) {
                world.notifyWorldEvent(RemoveEntityEvent(gameObject))
            }
        }
    }

    /**
     * Check if the bullet of an enemy is colliding with player or with an obstacle.
     *
     * Returns the [GameObject] which is colliding with the current bullet if there is any, or `null` otherwise.
     */
    private fun checkPlayerCollision(): GameObject? {
        return world.gameObjects
            .filter { it.type in PLAYER_COLLISION_TYPES }
            .firstOrNull { it.hitBox.isCollidingWith(gameObject.hitBox) }
    }

    /**
     * Check if the bullet of an enemy is colliding with enemies or with an
     * obstacle.
     *
     * Returns the [GameObject] which is colliding with the current bullet if there is any, or `null` otherwise.
     */
    private fun checkEnemyCollision(): GameObject? {
        return world.gameObjects
            .filter { it.type in ENEMY_COLLISION_TYPES }
            .firstOrNull { it.hitBox.isCollidingWith(gameObject.hitBox) }
    }

    /**
     * This method moves the bullet with a translation based on velocity and [deltaTime].
     */
    private fun move(deltaTime: Double) {
        gameObject.position += velocity.multiply(deltaTime * TIME_MULTIPLIER)
    }

    companion object {
        /**
         * Default speed of bullets.
         */
        private const val SPEED = 10

        /**
         * Factor to adjust time measure.
         */
        private const val TIME_MULTIPLIER = 0.1

        private val PLAYER_COLLISION_TYPES = listOf(PLAYER, ROCK, ITEM)
        private val ENEMY_COLLISION_TYPES = listOf(
            ENEMY_DRUNK, ENEMY_DRUNKSPINNER, ENEMY_LURKER, ENEMY_SHOOTER,
            ENEMY_SPINNER, ROCK, ITEM
        )
    }
}
