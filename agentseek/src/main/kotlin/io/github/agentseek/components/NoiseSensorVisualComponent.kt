package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseSensor: NoiseSensorComponent
    private val timer = TimerImpl(DEFAULT_SUSPICIOUS_TIME_MILLIS.toLong())
    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
        timer.startTimer()
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseSensor.getNoiseFound() && timer.isElapsed()) {
            timer.restart()
            VFX.fadingText(
                worldPosition = gameObject.position, text = "?", color = Color.BLACK, size = 40, durationMillis = 3000
            )
        }
    }

    private companion object {
        const val DEFAULT_SUSPICIOUS_TIME_MILLIS = 3000
    }
}