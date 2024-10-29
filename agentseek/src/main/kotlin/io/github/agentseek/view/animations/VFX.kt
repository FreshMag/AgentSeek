package io.github.agentseek.view.animations

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.RenderingContext
import io.github.agentseek.view.animations.AnimationUtilities.renderTextAnimationIteration
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import java.awt.Graphics2D
import kotlin.time.Duration.Companion.milliseconds


object VFX {
    private const val DEFAULT_SCHEDULE_TIME_MILLIS = 50

    fun expandingCircle(
        worldPosition: Point2d,
        color: Color,
        speed: Int,
        maxRadius: Double = 10.0
    ) {
        val context: RenderingContext<Graphics2D> = GameEngine.view?.getRenderingContext() ?: return
        var startingCircle = Circle2d(0.0)
        startingCircle.center = worldPosition
        val radiusStep = 1.0
        var iteration = 0
        val maxIterations = maxRadius.toInt()
        GameEngine.schedule(DEFAULT_SCHEDULE_TIME_MILLIS.milliseconds / speed) {
            if (startingCircle.radius >= maxRadius) {
                cancel()
            } else {
                context.strokeCircle(
                    startingCircle,
                    AnimationUtilities.fadingColor(color, iteration, maxIterations),
                )
                iteration++
                startingCircle = startingCircle.copy(radius = startingCircle.radius + radiusStep)
                startingCircle.center = worldPosition
            }
        }
    }

    fun fadingText(worldPosition: Point2d, text: String, color: Color, size: Int, durationMillis: Int) {
        val view = GameEngine.view ?: return
        val context: RenderingContext<Graphics2D> = view.getRenderingContext() ?: return
        val maxIterations = durationMillis / DEFAULT_SCHEDULE_TIME_MILLIS
        var iteration = 0
        val screenPoint = view.camera.toCameraPoint(worldPosition)
        GameEngine.schedule(DEFAULT_SCHEDULE_TIME_MILLIS.milliseconds) {
            if (iteration < maxIterations) {
                context.renderTextAnimationIteration(screenPoint, text, color, size, iteration, maxIterations)
                iteration++
            } else {
                cancel()
            }
        }
    }
}