package io.github.agentseek.components

import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities.radians
import io.github.agentseek.util.FastEntities.vector
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillGradientCone
import java.awt.Color
import kotlin.time.Duration

class SightSensorComponent(
    gameObject: GameObject,
    val coneLength: Double,
    val coneApertureRadians: Double,
    private val namesWhitelist: Set<String> = setOf(),
) :
    AbstractComponent(gameObject), Sensor<List<SightSensorComponent.Perception>> {

    data class Perception(val gameObject: GameObject, val distance: Double)

    private val sensorCollider: Collider = Collider.ConeCollider(coneApertureRadians, coneLength, 0.0, gameObject)
    private var lastPos = gameObject.position
    private var reactions = listOf<(List<Perception>) -> Unit>()
    private var isObjectInSight = false

    /**
     * The color of the cone of light projected by this sensor.
     */
    var lightColor: Color = Config.Components.sightSensorDefaultColor

    val directionOfSight: Vector2d
        get() = vector(1.0, 0).rotateRadians((sensorCollider.shape as Cone2d).rotation)

    override fun init() {
        sensorCollider.position = gameObject.position
        gameObject.attachRenderer { _, context ->
            context?.fillGradientCone(sensorCollider.shape as Cone2d, lightColor, Color(255, 255, 0, 0))
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
        if (colliding.isNotEmpty() && colliding.any { it.gameObject.name.lowercase() in namesWhitelist }) {
            val perceptions: List<Perception> = colliding.mapNotNull {
                val go = it.gameObject
                val intersection = gameObject.castRay(go).firstIntersecting
                if (intersection?.gameObject?.id == go.id) {
                    val perception = Perception(intersection.gameObject, intersection.distance)
                    isObjectInSight = true
                    return@mapNotNull perception
                }
                return@mapNotNull null
            }
            reactions.forEach { it(perceptions) }
        } else {
            if (isObjectInSight) {
                reactions.forEach { it(emptyList()) }
            }
            isObjectInSight = false
        }
    }

    override fun addReaction(reaction: (List<Perception>) -> Unit) {
        reactions += reaction
    }

    /**
     * Rotates the cone of the sensor
     */
    fun rotate(degrees: Number) {
        val shape = (sensorCollider.shape as? Cone2d) ?: return
        shape.rotation += radians(degrees)
    }

    /**
     * Sets the direction of the cone of the sensor
     */
    fun setDirection(direction: Vector2d) {
        val shape = (sensorCollider.shape as? Cone2d) ?: return
        shape.rotation = direction.angle()
    }

    fun getIsObjectInSight() = isObjectInSight
}