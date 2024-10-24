package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.strokeShape
import kotlin.time.Duration

class DistanceSensorComponent(
    gameObject: GameObject,
    radius: Double
) : AbstractComponent(gameObject) {
    private val sensorCollider: Collider = Collider.CircleCollider(radius, gameObject)
    private var previousDirection = Vector2d.zero()

    override fun init() {
        sensorCollider.center = gameObject.center()
        gameObject.attachRenderer { _, context ->
            context?.strokeShape(sensorCollider.shape)
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        sensorCollider.center = gameObject.center()
        val colliding = sensorCollider.findColliding()
        if (colliding.isNotEmpty()) {
            val checked = mutableSetOf<String>()
            val danger = colliding.fold(Vector2d.zero()) { resultant, collider ->
                val go = collider.gameObject
                val ray = gameObject.castRay(go)
                val intersection = ray.firstIntersecting ?: return@fold resultant
                if (intersection.gameObject.id !in checked) {
                    resultant + (ray.direction * intersection.distance)
                } else {
                    resultant
                }
            }
            val clockwise = danger.rotateDegrees(-135.0)
            val antiClockwise = danger.rotateDegrees(135.0)
            previousDirection =
                if (clockwise.angleWith(previousDirection) < antiClockwise.angleWith(previousDirection)) {
                    clockwise
                } else {
                    antiClockwise
                }
            gameObject.rigidBody.velocity =  previousDirection * 1.25
        } else {
            previousDirection = Vector2d(1.0, 1.0)
            gameObject.rigidBody.velocity = previousDirection
        }
    }
}