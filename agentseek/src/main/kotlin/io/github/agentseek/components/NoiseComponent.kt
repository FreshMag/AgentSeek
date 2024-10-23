package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.RenderingContext
import io.github.agentseek.view.SimpleRenderer
import io.github.agentseek.view.utilities.Rendering.strokeShape
import java.awt.Graphics2D
import kotlin.time.Duration

class NoiseComponent(gameObject: GameObject, radius: Double) : AbstractComponent(gameObject) {
    private val noiseCircleRigidBody = RigidBody.CircleRigidBody(radius, gameObject)
    private var isEmittingNoise = false
    private var lastPos = gameObject.position
    override fun init() {
        noiseCircleRigidBody.shape.position = gameObject.position
        val originalRenderer = gameObject.renderer
        gameObject.renderer = object : SimpleRenderer() {
            override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
                renderingContext?.strokeShape(noiseCircleRigidBody.shape)
                originalRenderer.applyOnView(gameObject)
            }
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseCircleRigidBody.shape.center = gameObject.center()
        if (lastPos != gameObject.position) {
            lastPos = gameObject.position
            isEmittingNoise = true
        } else {
            isEmittingNoise = false
        }
    }

    fun getNoiseRigidBody(): RigidBody? {
        return if (isEmittingNoise) noiseCircleRigidBody
        else null
    }
}