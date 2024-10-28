package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseSensorComponent::class)
class NoiseSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var noiseSensor: NoiseSensorComponent
    private var lastFoundMillis: Long? = null

    override fun init() {
        noiseSensor = gameObject.getComponent<NoiseSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        if (noiseSensor.getNoiseFound() && hasTImeElapsed()) {
            lastFoundMillis = System.currentTimeMillis()
            VFX.fadingText(gameObject.position, "?", Color.BLACK, 40)
        }
    }

    private fun hasTImeElapsed(): Boolean {
        val currentTime = System.currentTimeMillis()
        return lastFoundMillis == null || currentTime - lastFoundMillis!! >= DEFAULT_TIME_BETWEEN_UPDATES
    }

    companion object {
        const val DEFAULT_TIME_BETWEEN_UPDATES = 3000
    }
}