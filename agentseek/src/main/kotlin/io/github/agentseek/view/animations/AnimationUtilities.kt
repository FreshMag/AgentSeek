package io.github.agentseek.view.animations

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.view.RenderingContext
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

/**
 * Utility functions for rendering animations.
 */
object AnimationUtilities {

    /**
     * Fades a color to transparent based on the given [iteration] with respect to [maxIterations].
     */
    fun fadingColor(startColor: Color, iteration: Int, maxIterations: Int): Color =
        Color(
            startColor.red,
            startColor.green,
            startColor.blue,
            255 - (255 * iteration / maxIterations)
        )

    /**
     * Calculates an incremental rotation based on the given [iteration] with respect to [maxIterations], starting from
     * [startAngleDegrees] and ending at [endAngleDegrees].
     */
    fun incrementalRotation(
        startAngleDegrees: Double,
        endAngleDegrees: Double,
        iteration: Int,
        maxIterations: Int
    ): Double {
        val clampedIteration = iteration.coerceIn(0, maxIterations)
        val progress = clampedIteration.toDouble() / maxIterations
        return startAngleDegrees + progress * (endAngleDegrees - startAngleDegrees)
    }

    /**
     * Renders a text animation iteration at the given [position] with the given [text], [originalColor], [fontSize],
     * [iteration], and [maxIterations].
     */
    fun RenderingContext<Graphics2D>.renderTextAnimationIteration(
        position: Point2d,
        text: String,
        originalColor: Color,
        fontSize: Int,
        iteration: Int,
        maxIterations: Int
    ) {
        render {
            val previousFont = it.font
            val previousPaint = it.paint
            val originalTransform: AffineTransform = it.transform

            val font = Font(Config.Rendering.textFont, Font.PLAIN, fontSize)
            it.font = font
            it.paint = fadingColor(originalColor, iteration, maxIterations)
            val angle = incrementalRotation(0.0, -15.0, iteration, maxIterations)
            it.rotate(Math.toRadians(angle), position.x, position.y)
            it.drawString(text, position.x.toFloat(), position.y.toFloat())

            it.transform = originalTransform
            it.paint = previousPaint
            it.font = previousFont
        }
    }
}