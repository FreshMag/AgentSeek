package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.common.ComponentsUtils
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.physics.Collider
import io.github.agentseek.view.Camera
import io.github.agentseek.view.animations.VFX
import java.awt.Color
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class MouseNoiseEmitterComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    companion object {
        const val STANDARD_NOISE_RADIUS = 5.0
        const val STANDARD_NOISE_DURATION_MILLIS = 1000L
        const val STANDARD_NEAR_PLAYER_DISTANCE = 15.0
    }

    private var isEmittingNoise = false
    private var noiseEmitterCollider: Collider? = null
    private val timer = TimerImpl(STANDARD_NOISE_DURATION_MILLIS)
    private val camera: Camera
        get() = GameEngine.view?.camera!!


    override fun onUpdate(deltaTime: Duration) {
        val mouse = Input.mouseClicked()
        mouse?.let {
            val worldPoint = camera.toWorldPoint(mouse)
            if (ComponentsUtils.isPointWithinDistance(
                    worldPoint,
                    Point2d(gameObject.position.x, gameObject.position.y),
                    STANDARD_NEAR_PLAYER_DISTANCE
                ) && (!timer.isStarted || timer.isElapsed())
            ) {
                timer.restart()
                noiseEmitterCollider = Collider.CircleCollider(STANDARD_NOISE_RADIUS, gameObject)
                noiseEmitterCollider?.center = worldPoint
                isEmittingNoise = true
                VFX.fadingText(worldPoint, "BUM", Color.BLACK, 40, STANDARD_NOISE_DURATION_MILLIS.toInt())
            }
        }
        if (timer.isElapsed()) {
            isEmittingNoise = false
            noiseEmitterCollider = null
        }
    }

    fun getNoiseEmitterCollider(): Collider? {
        return if (isEmittingNoise) noiseEmitterCollider
        else null
    }
}