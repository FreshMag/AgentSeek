package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.center
import kotlin.time.Duration

@Requires(DistanceSensorComponent::class)
class FieldMovementComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    lateinit var sensor: DistanceSensorComponent
    private var previousDirection = Vector2d.zero()
    private val forwardDirection
        get() = Point2d(50.0, 50.0) - gameObject.center()

    override fun init() {
        sensor = gameObject.getComponent<DistanceSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        val distances = sensor.getDistancesResultant()
        val direction: Vector2d = previousDirection +
            if (distances != Vector2d.zero()) {
                val clockwise = distances.rotateDegrees(-TANGENTIAL_DEGREES)
                val antiClockwise = distances.rotateDegrees(TANGENTIAL_DEGREES)
                val tangentialVector =
                    if (clockwise.angleWith(forwardDirection) > antiClockwise.angleWith(forwardDirection)) {
                        antiClockwise
                    } else {
                        clockwise
                    }
                (tangentialVector * DANGER_COEFFICIENT)
            } else {
                forwardDirection
            }
        previousDirection = direction.normalized()
        gameObject.rigidBody.velocity = previousDirection * MAX_VELOCITY
    }

    companion object {
        const val TANGENTIAL_DEGREES = 115.0
        const val MAX_VELOCITY = 3.0
        const val DANGER_COEFFICIENT = 0.3
    }
}