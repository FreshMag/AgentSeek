package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import kotlin.math.absoluteValue
import kotlin.time.Duration

/**
 * Various levels of noise emission.
 */
private enum class NoiseLevel {
    LOW, DEFAULT, HIGH
}

/**
 * Component that emits noise based on the speed of the game object.
 */
class NoiseEmitterComponent(gameObject: GameObject, private val radius: Double) : AbstractComponent(gameObject) {

    private var isEmittingNoise = false
    private var lastPos = gameObject.center()
    private var noiseEmitterCollider: Collider = Collider.CircleCollider(radius, gameObject)

    override fun init() {
        noiseEmitterCollider.center = gameObject.center()
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.strokeCircle(
                noiseEmitterCollider.shape as Circle2d, color = Config.VisualComponents.noiseEmitterColor
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

    /**
     * Returns the noise emitter collider if the game object is emitting noise, otherwise null.
     */
    fun getNoiseEmitterCollider(): Collider? {
        return if (isEmittingNoise) noiseEmitterCollider
        else null
    }

    /**
     * Returns the noise level based on the speed of the game object.
     */
    private fun getNoiseLevel(): NoiseLevel {
        return if (gameObject.rigidBody.velocity.x.absoluteValue >= Config.Components.noiseEmitterSpeedThreshold
            || gameObject.rigidBody.velocity.y.absoluteValue >= Config.Components.noiseEmitterSpeedThreshold
        ) {
            NoiseLevel.DEFAULT
        } else {
            NoiseLevel.LOW
        }
    }
}