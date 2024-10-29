package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillGradientCircle
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import kotlin.time.Duration

class NoiseEmitterComponent(gameObject: GameObject, radius: Double) : AbstractComponent(gameObject) {
    private val noiseEmitterCollider: Collider = Collider.CircleCollider(radius, gameObject)
    private var isEmittingNoise = false
    private var lastPos = gameObject.position
    override fun init() {
        noiseEmitterCollider.center = gameObject.center()
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.fillGradientCircle(
                noiseEmitterCollider.shape as Circle2d, Color(245, 154, 165), Color(255, 255, 0, 0)
            )
            renderingContext?.strokeCircle(
                noiseEmitterCollider.shape as Circle2d, color = Color.BLACK
            )
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseEmitterCollider.center = gameObject.center()
        if (lastPos != gameObject.center()) {
            lastPos = gameObject.center()
            isEmittingNoise = true
        } else {
            isEmittingNoise = false
        }
    }

    fun getNoiseEmitterCollider(): Collider? {
        return if (isEmittingNoise) noiseEmitterCollider
        else null
    }
}