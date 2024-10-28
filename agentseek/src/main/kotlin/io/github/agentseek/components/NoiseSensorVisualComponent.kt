package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseSensor: NoiseSensorComponent
    private var lastNoiseHeardTimeMillis: Long? = null

    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseSensor.getNoiseFound() && hasTimeElapsed()) {
            lastNoiseHeardTimeMillis = System.currentTimeMillis()
            VFX.fadingText(gameObject.position, "?", Color.BLACK, 40, DEFAULT_SUSPICIOUS_TIME_MILLIS)
        }
    }

    private fun hasTimeElapsed(): Boolean {
        val currentTime = System.currentTimeMillis()
        return lastNoiseHeardTimeMillis == null || currentTime - lastNoiseHeardTimeMillis!! >= DEFAULT_SUSPICIOUS_TIME_MILLIS
    }

    companion object {
        const val DEFAULT_SUSPICIOUS_TIME_MILLIS = 3000
    }
}