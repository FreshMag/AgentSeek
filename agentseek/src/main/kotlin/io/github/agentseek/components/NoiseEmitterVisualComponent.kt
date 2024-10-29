package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class NoiseEmitterVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private var lastNoiseEmittedTimeMillis: Long? = null
    override fun onUpdate(deltaTime: Duration) {
        gameObject.getComponent<NoiseEmitterComponent>()!!.getNoiseEmitterCollider()?.let {
            if (hasTimeElapsed()) {
                lastNoiseEmittedTimeMillis = System.currentTimeMillis()
                VFX.expandingCircle(
                    worldPosition = it.center, color = Color.black, speed = 1, maxRadius = (it.shape as Circle2d).radius
                )
            }
        }

    }

    private fun hasTimeElapsed(): Boolean {
        val currentTime = System.currentTimeMillis()
        return lastNoiseEmittedTimeMillis == null || currentTime - lastNoiseEmittedTimeMillis!! >= 300
    }
}