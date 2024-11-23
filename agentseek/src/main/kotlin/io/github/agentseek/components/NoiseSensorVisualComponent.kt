package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private var noiseSensor: NoiseSensorComponent? = null
    private var noiseFound: Boolean = false
    private val timer = TimerImpl(Config.VisualComponents.noiseSensorDefaultSuspiciousTimeMillis)
    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensor?.addReaction {
            noiseFound = it.isNotEmpty()
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseFound && (timer.isElapsed() || !timer.isStarted)) {
            timer.restart()
            VFX.fadingText(
                worldPosition = gameObject.position,
                text = Config.VisualComponents.noiseSensorText,
                color = Config.VisualComponents.noiseSensorColor,
                size = Config.VisualComponents.noiseSensorFontSize,
                durationMillis = Config.VisualComponents.noiseSensorDefaultSuspiciousTimeMillis.toInt()
            )
        }
    }
}