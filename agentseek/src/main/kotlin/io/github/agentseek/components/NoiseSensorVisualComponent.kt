package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private var noiseSensor: NoiseSensorComponent? = null
    private val timer = TimerImpl(DEFAULT_SUSPICIOUS_TIME_MILLIS.toLong())
    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
        timer.startTimer()
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseSensor?.getNoiseFound() == true && timer.isElapsed()) {
            timer.restart()
            VFX.fadingText(
                worldPosition = gameObject.position,
                text = "?",
                color = Color.BLACK,
                size = 40,
                durationMillis = DEFAULT_SUSPICIOUS_TIME_MILLIS
            )
        }
    }

    private companion object {
        const val DEFAULT_SUSPICIOUS_TIME_MILLIS = 3000
    }
}