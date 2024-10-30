package io.github.agentseek.components

import io.github.agentseek.common.Cone2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillGradientCone
import java.awt.Color
import kotlin.time.Duration

class SightSensorComponent(gameObject: GameObject, coneLength: Double, coneAperture: Double) :
    AbstractComponent(gameObject), Sensor<SightSensorComponent.Perception> {

    data class Perception(val gameObject: GameObject, val distance: Double)

    private val sensorCollider: Collider = Collider.ConeCollider(coneAperture, coneLength, 0.0, gameObject)
    private var lastPos = gameObject.position
    private var reactions = listOf<(Perception) -> Unit>()
    private var isSeeing = false

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
            colliding.forEach {
                val go = it.gameObject
                val intersection = gameObject.castRay(go).firstIntersecting
                if (intersection?.gameObject?.id == go.id) {
                    val perception = Perception(intersection.gameObject, intersection.distance)
                    reactions.forEach { it(perception) }
                    isSeeing = true
                }
            }
        } else {
            isSeeing = false
        }
    }

    override fun addReaction(reaction: (Perception) -> Unit) {
        reactions += reaction
    }

    fun getIsSeeing() = isSeeing
}