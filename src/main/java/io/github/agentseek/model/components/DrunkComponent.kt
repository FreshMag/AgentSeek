package io.github.agentseek.model.components

import io.github.agentseek.common.GameMath
import io.github.agentseek.common.Vector2d
import io.github.agentseek.model.Type
import io.github.agentseek.model.World

/**
 * A class that provides the Component for drunk enemy type.
 */
class DrunkComponent(world: World) : AbstractComponent(world) {
    private val velocity: Vector2d = Vector2d(0.0, 0.0)
    private val angleAdjustRate: Double = GameMath.randomInRange(0.0, ANGLE_ADJUST)
    private var directionAngle: Double = GameMath.toDegrees(GameMath.randomInRange(-1.0, 1.0) * Math.PI * 2)
    private val tx: Double = GameMath.randomInRange(1000.0, 10000.0)

    override fun onUpdate(deltaTime: Double) {
        adjustAngle()
        move(deltaTime)
        checkScreenBounds()
    }

    /**
     * Adjusts the direction angle.
     */
    private fun adjustAngle() {
        if (GameMath.randomSampling(angleAdjustRate)) {
            // never rotate further than 20 degrees
            directionAngle += GameMath.toDegrees(GameMath.smoothNoise(tx) - 0.5).coerceAtMost(ROTATION_ANGLE)
        }
    }

    /**
     * Generates new direction vector to move to. [deltaTime] is the time elapsed between each frame.
     */
    private fun move(deltaTime: Double) {
        val directionVector: Vector2d = Vector2d.fromAngle(directionAngle).mulLocal(MOVE_SPEED)
        velocity.addLocal(directionVector).normalized().mulLocal(deltaTime * TPF)
        gameObject.position += this.velocity
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

    private val isCollidingWithRock: Boolean
        /**
         * Checks if the GameObject collided with a rock.
         */
        get() =
            world.gameObjects
                .filter { it.type == Type.ROCK }
                .any { it.hitBox.isCollidingWith(gameObject.hitBox) }


    companion object {
        /**
         * Value used to calculate maximum range value for random angle adjust rate.
         */
        private const val ANGLE_ADJUST = 0.15

        /**
         * Drunk enemy movement speed value.
         */
        private const val MOVE_SPEED = 0.05

        /**
         * Drunk enemy rotation angle value.
         */
        private const val ROTATION_ANGLE = 20.0

        /**
         * Time per frame multiplier.
         */
        private const val TPF = 0.01
    }
}
