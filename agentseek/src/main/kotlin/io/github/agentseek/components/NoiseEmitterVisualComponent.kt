package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class NoiseEmitterVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {

    override fun onUpdate(deltaTime: Duration) {
        gameObject.getComponent<NoiseEmitterComponent>()!!.getNoiseEmitterCollider()?.let {
            VFX.expandingCircle(
                worldPosition = it.center, color = Color.gray, speed = 3, maxRadius = (it.shape as Circle2d).radius + 1
            )
        }
    }
}