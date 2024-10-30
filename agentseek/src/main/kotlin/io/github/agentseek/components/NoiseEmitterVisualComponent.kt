package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class NoiseEmitterVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseEmitterComponent: NoiseEmitterComponent

    private val timer = TimerImpl(500)
    override fun init() {
        timer.startTimer()
        noiseEmitterComponent = gameObject.getComponent<NoiseEmitterComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseEmitterComponent.getNoiseEmitterCollider()?.let {
            if (timer.isElapsed()) {
                timer.restart()
                VFX.expandingCircle(
                    worldPosition = it.center, color = Color.black, speed = 1, maxRadius = (it.shape as Circle2d).radius
                )
            }
        }

    }
}