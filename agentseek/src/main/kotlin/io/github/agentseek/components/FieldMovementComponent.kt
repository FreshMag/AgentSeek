package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.drawVector
import kotlin.time.Duration

@Requires(DistanceSensorComponent::class)
class FieldMovementComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var sensor: DistanceSensorComponent
    private var previousDirection = Vector2d.zero()
    private var directionObjective: Vector2d = Vector2d.zero()
    private val forwardDirection
        get() = objective - gameObject.center()

    var objective: Point2d = Point2d(50.0, 50.0)

    private var isStopped: Boolean = false

    private var debugDirection = Vector2d.zero()
    private var debugDanger = Vector2d.zero()

    override fun init() {
        sensor = gameObject.getComponent<DistanceSensorComponent>()!!

        gameObject.attachRenderer { _, context ->
            context?.drawVector(gameObject.center(), debugDirection, java.awt.Color.PINK)
            context?.drawVector(gameObject.center(), debugDanger, java.awt.Color.CYAN)
            context?.drawVector(gameObject.center(), forwardDirection, java.awt.Color.ORANGE)
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        if (isStopped) {
            gameObject.rigidBody.velocity = Vector2d.zero()
            return
        }
        val distances = sensor.getDistancesResultant()
        debugDanger = distances
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
        debugDirection = direction * MAX_VELOCITY
        gameObject.rigidBody.velocity = previousDirection * MAX_VELOCITY
    }

    fun setDirection(direction: Vector2d) {
        directionObjective = direction.normalized()
    }

    fun stop() {
        isStopped = true
    }

    fun wakeUp() {
        isStopped = false
    }

    companion object {
        const val TANGENTIAL_DEGREES = 115.0
        const val MAX_VELOCITY = 2.0
        const val DANGER_COEFFICIENT = 0.75
    }
}