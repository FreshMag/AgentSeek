package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.RenderingContext
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import java.awt.Graphics2D
import kotlin.time.Duration.Companion.milliseconds

object VFX {

    fun expandingCircle(worldPosition: Point2d, color: Color, speed: Int, maxRadius: Double = 10.0) {
        val view = GameEngine.view ?: return
        val context: RenderingContext<Graphics2D> = view.getRenderingContext() ?: return
        var startingCircle = Circle2d(0.0)
        startingCircle.center = worldPosition
        val radiusStep = 1.0
        var iteration = 1
        val maxIterations = maxRadius.toInt()

        GameEngine.schedule(50.milliseconds / speed) {
            if (startingCircle.radius >= maxRadius) {
                cancel()
            } else {
                context.strokeCircle(
                    startingCircle,
                    Color(color.red, color.green, color.blue, 255 - (255 * iteration / maxIterations)),
                )
                iteration++
                startingCircle = startingCircle.copy(radius = startingCircle.radius + radiusStep)
                startingCircle.center = worldPosition
            }
        }
    }
}