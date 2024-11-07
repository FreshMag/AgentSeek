package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillGradientCircle
import java.awt.Color
import kotlin.time.Duration

class DistanceSensorComponent(
    gameObject: GameObject,
    private val radius: Double
) : AbstractComponent(gameObject) {
    private val sensorCollider: Collider = Collider.CircleCollider(radius, gameObject)

    override fun init() {
        sensorCollider.center = gameObject.center()
        gameObject.attachRenderer { _, context ->
            context?.fillGradientCircle(sensorCollider.shape as Circle2d, Color.RED, Color(255, 255, 0, 0))
        }
    }

    /**
     * Gets the resultant obtained adding all vectors of distances from nearby objects within [radius]
     */
    fun getDistancesResultant(): Vector2d {
        val colliding = sensorCollider.findColliding()
        return if (colliding.isNotEmpty()) {
            val checked = mutableSetOf<String>()
            val danger = colliding.fold(Vector2d.zero()) { resultant, collider ->
                val go = collider.gameObject
                val ray = gameObject.castRay(go)
                val intersection = ray.firstIntersecting ?: return@fold resultant
                if (intersection.gameObject.id !in checked) {
                    checked.add(intersection.gameObject.id)
                    resultant + (ray.direction * intersection.distance)
                } else {
                    resultant
                }
            }
            danger.normalized() * (radius - danger.module())
        } else {
            Vector2d.zero()
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        sensorCollider.center = gameObject.center()
    }
}