package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.common.ComponentsUtils
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.physics.Collider
import io.github.agentseek.view.Camera
import io.github.agentseek.view.animations.VFX
import kotlin.time.Duration

/**
 * MouseNoiseEmitterComponent is responsible for emitting noise when the player clicks on the screen.
 */
@Requires(NoiseEmitterComponent::class)
class MouseNoiseEmitterComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    private var isEmittingNoise = false
    private var noiseEmitterCollider: Collider? = null
    private val timer = TimerImpl(Config.Components.mouseEmitterNoiseDurationMillis)
    private val camera: Camera
        get() = GameEngine.view?.camera!!


    override fun onUpdate(deltaTime: Duration) {
        val mouse = Input.mouseClicked()
        mouse?.let {
            val worldPoint = camera.toWorldPoint(mouse)
            if (ComponentsUtils.isPointWithinDistance(
                    worldPoint,
                    Point2d(gameObject.position.x, gameObject.position.y),
                    Config.Components.mouseEmitterPlayerMaxDistance
                ) && (!timer.isStarted || timer.isElapsed())
            ) {
                timer.restart()
                noiseEmitterCollider = Collider.CircleCollider(Config.Components.mouseEmitterNoiseRadius, gameObject)
                noiseEmitterCollider?.center = worldPoint
                isEmittingNoise = true
                VFX.fadingText(
                    worldPoint,
                    Config.VisualComponents.mouseEmitterText,
                    Config.VisualComponents.mouseEmitterTextColor,
                    Config.VisualComponents.mouseEmitterFontSize,
                    Config.Components.mouseEmitterNoiseDurationMillis.toInt()
                )
            }
        }
        if (timer.isElapsed()) {
            isEmittingNoise = false
            noiseEmitterCollider = null
        }
    }

    /**
     * Returns the noise emitter collider if the player is emitting noise, otherwise null.
     */
    fun getNoiseEmitterCollider(): Collider? {
        return if (isEmittingNoise) noiseEmitterCollider
        else null
    }
}