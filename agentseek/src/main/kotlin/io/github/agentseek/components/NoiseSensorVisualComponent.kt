package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseSensor: NoiseSensorComponent
    private var noiseFound: Boolean = false
    private val timer = TimerImpl(DEFAULT_SUSPICIOUS_TIME_MILLIS.toLong())
    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensor.addReaction {
            noiseFound = it.isNotEmpty()
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseFound && (timer.isElapsed() || !timer.isStarted)) {
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