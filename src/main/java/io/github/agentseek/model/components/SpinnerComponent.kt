package io.github.agentseek.model.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.model.GameObject
import io.github.agentseek.model.World
import kotlin.math.cos
import kotlin.math.sin

/**
 * A class that provides the Component for spinner enemy type.
 */
class SpinnerComponent(world: World) : AbstractComponent(world) {
    private val shooting: ShootingComponent = ShootingComponent(world, 1.0)
    private var angle: Double = 0.0

    override fun onAdded(gameObject: GameObject) {
        super.onAdded(gameObject)
        shooting.onAdded(gameObject)
    }

    override fun onUpdate(deltaTime: Double) {
        shoot()
        checkAngle()
    }

    /**
     * A method that calls shoot method from ShootingComponent towards a direction
     * given internal rotation angle value if the shooter is able to shoot.
     */
    private fun shoot() {
        val direction: Vector2d = Vector2d(
            gameObject.position.x * cos(angle) - gameObject.position.y * sin(angle),
            gameObject.position.x * sin(angle) - gameObject.position.y * cos(angle),
        ).normalized() * BULLET_SPEED
        shooting.shoot(direction)
    }

    /**
     * Method that increases rotation angle value and resets it if angle > 360°.
     */
    private fun checkAngle() {
        angle += ANGLE_INCREASE
        if (angle >= Math.PI * 2) {
            angle = 0.0
        }
    }

    companion object {
        /**
         * Speed at which bullet entities move.
         */
        private const val BULLET_SPEED = 0.30

        /**
         * Rate at which rotation angle changes.
         */
        private const val ANGLE_INCREASE = Math.PI / 40
    }
}
