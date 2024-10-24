package io.github.agentseek.components

import io.github.agentseek.common.Cone2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.strokeShape
import kotlin.time.Duration

class SightSensorComponent(gameObject: GameObject, coneLength: Double, coneAperture: Double) :
    AbstractComponent(gameObject) {

    private val sensorRigidBody: RigidBody = RigidBody.ConeRigidBody(coneAperture, coneLength, 0.0, gameObject)
    private var lastPos = gameObject.position

    override fun init() {
        sensorRigidBody.shape.position = gameObject.position
        gameObject.attachRenderer { _, context ->
            context?.strokeShape(sensorRigidBody.shape)
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        sensorRigidBody.shape.position = gameObject.center()
        if (lastPos != gameObject.position) {
            val direction = gameObject.position - lastPos
            val angle = direction.angle()
            (sensorRigidBody.shape as? Cone2d)?.rotation = angle
            lastPos = gameObject.position
        }
        val colliding = sensorRigidBody.findColliding()
        if (colliding.isNotEmpty()) {
            colliding.forEach {
                val go = it.gameObject
                if (gameObject.castRay(go).firstIntersecting?.id == go.id) {
                    println("I'm seeing ${go.id}!")
                }
            }
        }
    }

}