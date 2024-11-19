package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.view.Camera
import io.github.agentseek.view.animations.VFX
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import kotlin.math.sqrt
import kotlin.time.Duration

@Requires(NoiseEmitterComponent::class)
class MouseNoiseEmitterComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    companion object {
        const val STANDARD_NOISE_RADIUS = 5.0
        const val STANDARD_NOISE_DURATION_MILLIS = 1000L
        const val STANDARD_NEAR_PLAYER_DISTANCE = 10.0
    }

    private var isEmittingNoise = false
    private var noiseEmitterCollider: Collider = Collider.CircleCollider(STANDARD_NOISE_RADIUS, gameObject)
    private val timer = TimerImpl(STANDARD_NOISE_DURATION_MILLIS)
    private val camera: Camera
        get() = GameEngine.view?.camera!!


    override fun onUpdate(deltaTime: Duration) {
        val mouse = Input.mouseClicked()
        mouse?.let {
            val worldPoint = camera.toWorldPoint(mouse)
            if (isClickNearPlayer(worldPoint)) {
                timer.restart()
                noiseEmitterCollider.center = worldPoint
                drawNoiseCircle()
                isEmittingNoise = true
                VFX.fadingText(worldPoint, "BUM", Color.BLACK, 40, STANDARD_NOISE_DURATION_MILLIS.toInt())
            } else if (timer.isElapsed()) {
                isEmittingNoise = false
            }
        }
    }

    fun getNoiseEmitterCollider(): Collider? {
        return if (isEmittingNoise) noiseEmitterCollider
        else null
    }

    private fun drawNoiseCircle() {
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.strokeCircle(
                noiseEmitterCollider.shape as Circle2d, color = Color.BLACK
            )
        }
    }

    private fun isClickNearPlayer(worldPoint: Point2d): Boolean {
        val dx = worldPoint.x - gameObject.position.x
        val dy = worldPoint.y - gameObject.position.y
        return sqrt(dx * dx + dy * dy) <= STANDARD_NEAR_PLAYER_DISTANCE
    }
}