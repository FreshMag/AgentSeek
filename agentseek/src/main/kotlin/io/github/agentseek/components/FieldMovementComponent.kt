package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.center
import kotlin.time.Duration

@Requires(DistanceSensorComponent::class)
class FieldMovementComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var sensor: DistanceSensorComponent
    private var previousDirection = Vector2d.zero()
    private var directionObjective: Vector2d = Vector2d.zero()
    private val forwardDirection
        get() = objective?.let { it - gameObject.center() } ?: directionObjective

    var objective: Point2d? = Point2d(50.0, 50.0)

    private var isStopped: Boolean = false

    override fun init() {
        sensor = gameObject.getComponent<DistanceSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        if (isStopped) {
            gameObject.rigidBody.velocity = Vector2d.zero()
            return
        }
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

    fun setDirection(direction: Vector2d) {
        objective = null
        directionObjective = direction.normalized()
    }

    fun stop() {
        isStopped = true
    }

    fun wakeUp() {
        isStopped = false
    }

    companion object {
        const val TANGENTIAL_DEGREES = 105.0
        const val MAX_VELOCITY = 2.0
        const val DANGER_COEFFICIENT = 1.5
    }
}