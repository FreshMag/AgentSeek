package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.center
import kotlin.time.Duration

/**
 * Component that moves the game object using field based movement inspired by robotics. Many sources contribute to the
 * calculation of a *field* that determines the game object direction and velocity.
 * The field is calculated using the distances from nearby objects and the objective set.
 */
@Requires(DistanceSensorComponent::class)
class FieldMovementComponent(
    gameObject: GameObject,
    var maxVelocity: Double = Config.FieldMovement.defaultMaxVelocity
) :
    AbstractComponent(gameObject) {
    private lateinit var sensor: DistanceSensorComponent
    private var previousDirection = Vector2d.zero()
    private val forwardDirection
        get() = objective - gameObject.center()

    /**
     * The objective point to reach.
     */
    var objective: Point2d = Point2d(50.0, 50.0)

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
                    val clockwise = distances.rotateDegrees(-Config.FieldMovement.rotationDegrees)
                    val antiClockwise = distances.rotateDegrees(Config.FieldMovement.rotationDegrees)
                    val tangentialVector =
                        if (clockwise.angleWith(forwardDirection) > antiClockwise.angleWith(forwardDirection)) {
                            antiClockwise
                        } else {
                            clockwise
                        }
                    (tangentialVector * Config.FieldMovement.dangerCoefficient)
                } else {
                    forwardDirection
                }
        previousDirection = direction.normalized()
        gameObject.rigidBody.velocity = previousDirection * maxVelocity
    }

    /**
     * Stops the movement of the game object.
     */
    fun stop() {
        isStopped = true
    }

    /**
     * Resumes the movement of the game object.
     */
    fun wakeUp() {
        isStopped = false
    }
}