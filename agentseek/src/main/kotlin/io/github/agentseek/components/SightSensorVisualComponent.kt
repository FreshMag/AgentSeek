package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(SightSensorComponent::class)
class SightSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private val timer = TimerImpl(DEFAULT_SUSPICIOUS_TIME_MILLIS.toLong())

    override fun init() {
        timer.startTimer()
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        if (sightSensorComponent.getIsObjectInSight() && timer.isElapsed()) {
            timer.restart()
            VFX.fadingText(
                worldPosition = gameObject.position,
                text = "!",
                color = Color.BLACK,
                size = 40,
                durationMillis = DEFAULT_SUSPICIOUS_TIME_MILLIS
            )
        }
    }

    private companion object {
        const val DEFAULT_SUSPICIOUS_TIME_MILLIS = 1000
    }
}