package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class NoiseEmitterVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseEmitterComponent: NoiseEmitterComponent

    private val timer = TimerImpl(Config.VisualComponents.noiseEmitterDurationMillis)
    override fun init() {
        timer.startTimer()
        noiseEmitterComponent = gameObject.getComponent<NoiseEmitterComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseEmitterComponent.getNoiseEmitterCollider()?.let {
            if (timer.isElapsed()) {
                timer.restart()
                VFX.expandingCircle(
                    worldPosition = it.center,
                    color = Config.VisualComponents.noiseEmitterColor,
                    speed = 1,
                    maxRadius = (it.shape as Circle2d).radius
                )
            }
        }

    }
}