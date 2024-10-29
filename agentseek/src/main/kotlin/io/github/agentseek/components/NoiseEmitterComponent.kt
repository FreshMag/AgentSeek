package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.center
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
        return if (gameObject.rigidBody.velocity.x >= STANDARD_VELOCITY || gameObject.rigidBody.velocity.y >= STANDARD_VELOCITY) {
            NoiseLevel.DEFAULT
        } else {
            NoiseLevel.LOW
        }
    }
}