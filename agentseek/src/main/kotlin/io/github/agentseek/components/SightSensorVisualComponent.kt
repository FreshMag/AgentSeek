package io.github.agentseek.components

import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.animations.VFX
import kotlin.time.Duration

/**
 * A visual component that displays a text when an object is detected by a sight sensor.
 */
@Requires(SightSensorComponent::class)
class SightSensorVisualComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private val timer = TimerImpl(Config.VisualComponents.sightSensorDefaultSuspiciousTimeMillis)

    override fun init() {
        timer.startTimer()
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
    }

    override fun onUpdate(deltaTime: Duration) {
        if (sightSensorComponent.getIsObjectInSight() && timer.isElapsed()) {
            timer.restart()
            VFX.fadingText(
                worldPosition = gameObject.position,
                text = Config.VisualComponents.sightSensorText,
                color = Config.VisualComponents.sightSensorTextColor,
                size = Config.VisualComponents.sightSensorFontSize,
                durationMillis = Config.VisualComponents.sightSensorDefaultSuspiciousTimeMillis.toInt()
            )
        }
    }
}