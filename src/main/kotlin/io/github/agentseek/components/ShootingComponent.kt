package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Timer
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.common.Vector2d
import io.github.agentseek.world.World

/**
 * A Component that lets you shoot.
 */
class ShootingComponent(world: World, private var attackSpeed: Double) : AbstractComponent(world) {
    private val timer: Timer = TimerImpl(1000.0 / this.attackSpeed) //Timer class uses milliseconds

    init {
        timer.startTimer()
    }

    /**
     * Shoots a bullet at given [origin] and with the given [velocity] only if attack speed's timer is elapsed.
     */
    fun shoot(velocity: Vector2d, origin: Point2d) {
        if (canShoot()) {
            world.notifyWorldEvent(BulletSpawnEvent(gameObject, origin, velocity))
            timer.startTimer()
        }
    }

    /**
     * Shoot a bullet at game object's position with the given [velocity] only if attack speed's timer is elapsed.
     */
    fun shoot(velocity: Vector2d) {
        if (canShoot()) {
            world.notifyWorldEvent(
                BulletSpawnEvent(
                    gameObject,
                    gameObject.hitBox.form.center, velocity
                )
            )
            timer.startTimer()
        }
    }

    /**
     * Multiplies attack speed by a [factor].
     */
    fun multiplyAtkSpeed(factor: Double) {
        this.attackSpeed *= factor
        timer.setWaitTime(1000.0 / this.attackSpeed)
    }

    /**
     * Increases attack speed linearly, adding an [amount] to the number of shoots per second.
     */
    fun increaseAtkSpeed(amount: Double) {
        this.attackSpeed += amount
        timer.setWaitTime(1000.0 / this.attackSpeed)
    }

    /**
     * Checks whether object can shoot or not.
     */
    private fun canShoot(): Boolean {
        return timer.isElapsed()
    }

    override fun onUpdate(deltaTime: Double) {
        // does nothing
    }
}
