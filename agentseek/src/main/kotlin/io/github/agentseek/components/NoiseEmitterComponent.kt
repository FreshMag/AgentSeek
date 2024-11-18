package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import kotlin.math.absoluteValue
import kotlin.time.Duration

private enum class NoiseLevel {
    LOW, DEFAULT, HIGH
}

class NoiseEmitterComponent(gameObject: GameObject, private val radius: Double) : AbstractComponent(gameObject) {
    companion object {
        const val STANDARD_VELOCITY = 2.0
    }

    private var isEmittingNoise = false
    private var lastPos = gameObject.center()
    private var noiseEmitterCollider: Collider = Collider.CircleCollider(radius, gameObject)

    override fun init() {
        noiseEmitterCollider.center = gameObject.center()
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.strokeCircle(
                noiseEmitterCollider.shape as Circle2d, color = Color.BLACK
            )
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        val noiseRadius = when (getNoiseLevel()) {
            NoiseLevel.LOW -> radius / 2
            NoiseLevel.DEFAULT -> radius
            NoiseLevel.HIGH -> radius * 2
        }
        noiseEmitterCollider = Collider.CircleCollider(noiseRadius, gameObject)
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

    private fun getNoiseLevel(): NoiseLevel {
        return if (gameObject.rigidBody.velocity.x.absoluteValue >= STANDARD_VELOCITY
            || gameObject.rigidBody.velocity.y.absoluteValue >= STANDARD_VELOCITY
        ) {
            NoiseLevel.DEFAULT
        } else {
            NoiseLevel.LOW
        }
    }
}