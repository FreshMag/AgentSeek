package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.world.World

/**
 * A class that provides the Component for shooter enemy type.
 */
class ShooterComponent(world: World) : AbstractComponent(world) {
    private val shootingComponent: ShootingComponent = ShootingComponent(world, 0.75)

    override fun onAdded(gameObject: GameObject) {
        super.onAdded(gameObject)
        shootingComponent.onAdded(gameObject)
    }

    override fun onUpdate(deltaTime: Double) {
        shoot()
    }

    /**
     * A method that calls shoot method from ShootingComponent if the shooter is
     * able to shoot.
     */
    private fun shoot() {
        shootingComponent.shoot(directionToPlayer)
    }

    private val directionToPlayer: Vector2d
        get() = Vector2d(player?.position ?: Point2d.origin(), gameObject.position).normalized() * BULLET_SPEED

    companion object {
        /**
         * Speed at which bullet entities move.
         */
        private const val BULLET_SPEED = 0.25
    }
}
