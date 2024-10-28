package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import kotlin.time.Duration

class NoiseSensorComponent(gameObject: GameObject, radius: Double) : AbstractComponent(gameObject) {
    private val noiseSensorCollider: Collider = Collider.CircleCollider(radius, gameObject)
    private var lastPos = gameObject.position
    private var noiseFound = false

    override fun init() {
        noiseSensorCollider.shape.center = gameObject.center()
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseSensorCollider.shape.center = gameObject.center()
        if (lastPos != gameObject.center()) lastPos = gameObject.center()
        gameObject.otherGameObjects().forEach { go ->
            go.getComponent<NoiseEmitterComponent>()?.let { noiseComponent ->
                noiseComponent.getNoiseEmitterCollider()?.let {
                    if (it.isCollidingWith(noiseSensorCollider)) {
                        noiseFound = true
                    }
                } ?: run { noiseFound = false }
            } ?: run { noiseFound = false }
        }
    }

    fun getNoiseFound(): Boolean = noiseFound
}