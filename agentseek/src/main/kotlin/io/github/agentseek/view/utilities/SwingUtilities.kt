package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Point2d
import java.awt.*
import java.awt.geom.Point2D
import kotlin.math.cos
import kotlin.math.sin


object SwingUtilities {

    /**
     * Utility function to perform a [transformation] on the graphics 2D while resetting previous settings of the
     * graphic 2D after.
     */
    private fun graphics2dTransformationWithReset(transformation: (Graphics2D) -> Unit): (Graphics2D) -> Unit = { g ->
        val originalTransform = g.transform
        val originalPaint = g.paint
        val originalStroke: Stroke = g.stroke
        transformation(g)
        g.transform = originalTransform
        g.paint = originalPaint
        g.stroke = originalStroke
    }

    /**
     * Extracts data from [cone] and performs side effects on the [graphics2D] to set up the drawing of the cone
     */
    private fun setupConeRadiusArcAngle(cone: Cone2d, graphics2D: Graphics2D): Pair<Double, Double> {
        graphics2D.translate(cone.vertex.x, cone.vertex.y)
        graphics2D.rotate(cone.rotation)

        return Pair(cone.length, Math.toDegrees(cone.angle))
    }

    /**
     * Extracted function to draw the edges line of a cone
     */
    private fun drawConeLines(cone: Cone2d, graphics2D: Graphics2D) {
        val halfAngle = cone.angle / 2
        val startX = cos(-halfAngle) * cone.length
        val startY = sin(-halfAngle) * cone.length
        val endX = cos(halfAngle) * cone.length
        val endY = sin(halfAngle) * cone.length

        graphics2D.drawLine(0, 0, startX.toInt(), startY.toInt())
        graphics2D.drawLine(0, 0, endX.toInt(), endY.toInt())
    }

    /**
     * Strokes a cone with a [color]
     */
    fun strokeCone(graphics2D: Graphics2D, cone: Cone2d, color: Color) {
        graphics2dTransformationWithReset { g ->
            val (radius, arcAngle) = setupConeRadiusArcAngle(cone, g)

            g.color = color
            g.drawArc(
                (-radius).toInt(),
                (-radius).toInt(),
                (radius * 2).toInt(),
                (radius * 2).toInt(),
                (-arcAngle / 2).toInt(),
                arcAngle.toInt()
            )

            drawConeLines(cone, g)
        }(graphics2D)
    }

    /**
     * Fills a cone with a linear gradient starting from [startColor] to [endColor]
     */
    fun fillConeWithGradient(graphics2D: Graphics2D, cone: Cone2d, startColor: Color, endColor: Color) {
        graphics2dTransformationWithReset { g ->
            val (radius, arcAngle) = setupConeRadiusArcAngle(cone, g)

            val gradientStart = Point2d(0.0, 0.0)
            val gradientEnd = Point2d(cos(0.0) * radius, sin(0.0) * radius)

            val gradient = GradientPaint(
                gradientStart.x.toFloat(), gradientStart.y.toFloat(), startColor,
                gradientEnd.x.toFloat(), gradientEnd.y.toFloat(), endColor
            )

            g.paint = gradient

            g.fillArc(
                (-radius).toInt(),
                (-radius).toInt(),
                (radius * 2).toInt(),
                (radius * 2).toInt(),
                (-arcAngle / 2).toInt(),
                arcAngle.toInt()
            )

            drawConeLines(cone, g)
        }(graphics2D)
    }

    /**
     * Fills a circle with a radial gradient starting from [startColor] in the center and [endColor] on the border
     */
    fun fillCircleWithGradient(graphics2D: Graphics2D, circle2d: Circle2d, startColor: Color, endColor: Color) {
        graphics2dTransformationWithReset { g ->
            val (centerX, centerY) = circle2d.center
            val center = Point2D.Double(centerX, centerY)
            val dist = floatArrayOf(0.0f, 1.0f)
            val colors = arrayOf(startColor, endColor)

            val radialGradient = RadialGradientPaint(
                center,
                circle2d.radius.toFloat(),
                dist,
                colors
            )

            g.paint = radialGradient

            g.fillOval(
                (centerX - circle2d.radius).toInt(),
                (centerY - circle2d.radius).toInt(),
                (circle2d.radius * 2).toInt(),
                (circle2d.radius * 2).toInt()
            )
        }(graphics2D)
    }
}