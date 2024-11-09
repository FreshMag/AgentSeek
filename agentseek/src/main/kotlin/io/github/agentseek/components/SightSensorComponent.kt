package io.github.agentseek.components

import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillGradientCone
import java.awt.Color
import kotlin.time.Duration

class SightSensorComponent(gameObject: GameObject, coneLength: Double, coneApertureRadians: Double) :
    AbstractComponent(gameObject), Sensor<List<SightSensorComponent.Perception>> {

    data class Perception(val gameObject: GameObject, val distance: Double, val enemyPosition: Point2d)

    private val sensorCollider: Collider = Collider.ConeCollider(coneApertureRadians, coneLength, 0.0, gameObject)
    private var lastPos = gameObject.position
    private var reactions = listOf<(List<Perception>) -> Unit>()
    private var isObjectInSight = false

    override fun init() {
        sensorCollider.position = gameObject.position
        gameObject.attachRenderer { _, context ->
            context?.fillGradientCone(sensorCollider.shape as Cone2d, Color.YELLOW, Color(255, 255, 0, 0))
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        sensorCollider.position = gameObject.center()
        if (lastPos != gameObject.position) {
            val direction = gameObject.position - lastPos
            val angle = direction.angle()
            (sensorCollider.shape as? Cone2d)?.rotation = angle
            lastPos = gameObject.position
        }
        val colliding = sensorCollider.findColliding()
        if (colliding.isNotEmpty()) {
            val perceptions: List<Perception> = colliding.mapNotNull {
                val go = it.gameObject
                val intersection = gameObject.castRay(go).firstIntersecting
                if (intersection?.gameObject?.id == go.id) {
                    val perception = Perception(intersection.gameObject, intersection.distance, go.position)
                    isObjectInSight = true
                    return@mapNotNull perception
                }
                return@mapNotNull null
            }
            reactions.forEach { it(perceptions) }
        } else {
            if (isObjectInSight) {
                reactions.forEach { it(emptyList<Perception>()) }
            }
            isObjectInSight = false
        }
    }

    override fun addReaction(reaction: (List<Perception>) -> Unit) {
        reactions += reaction
    }

    fun getIsObjectInSight() = isObjectInSight
}