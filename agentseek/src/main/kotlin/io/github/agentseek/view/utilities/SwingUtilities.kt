package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Point2d
import java.awt.*
import java.awt.geom.Point2D
import kotlin.math.cos
import kotlin.math.sin


object SwingUtilities {

    fun strokeCone(g: Graphics2D, cone: Cone2d) {
        val originalTransform = g.transform
        val originalColor: Color = g.color
        val originalStroke: Stroke = g.stroke


        g.translate(cone.vertex.x, cone.vertex.y)

        g.rotate(cone.rotation)

        val radius = cone.length
        val arcAngle = Math.toDegrees(cone.angle)

        g.color = Color.RED
        g.drawArc(
            (-radius).toInt(),
            (-radius).toInt(),
            (radius * 2).toInt(),
            (radius * 2).toInt(),
            (-arcAngle / 2).toInt(),
            arcAngle.toInt()
        )

        val halfAngle = cone.angle / 2
        val startX = cos(-halfAngle) * radius
        val startY = sin(-halfAngle) * radius
        val endX = cos(halfAngle) * radius
        val endY = sin(halfAngle) * radius

        g.drawLine(0, 0, startX.toInt(), startY.toInt())
        g.drawLine(0, 0, endX.toInt(), endY.toInt())

        g.transform = originalTransform
        g.color = originalColor
        g.stroke = originalStroke
    }

    fun fillConeWithGradient(g: Graphics2D, cone: Cone2d, startColor: Color, endColor: Color) {
        val originalTransform = g.transform
        val originalPaint = g.paint
        val originalStroke: Stroke = g.stroke

        g.translate(cone.vertex.x, cone.vertex.y)
        g.rotate(cone.rotation)

        val radius = cone.length
        val arcAngle = Math.toDegrees(cone.angle)

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

        val halfAngle = cone.angle / 2
        val startX = cos(-halfAngle) * radius
        val startY = sin(-halfAngle) * radius
        val endX = cos(halfAngle) * radius
        val endY = sin(halfAngle) * radius

        g.drawLine(0, 0, startX.toInt(), startY.toInt())
        g.drawLine(0, 0, endX.toInt(), endY.toInt())

        g.transform = originalTransform
        g.paint = originalPaint
        g.stroke = originalStroke
    }

    fun fillCircleWithGradient(g: Graphics2D, circle2d: Circle2d, startColor: Color, endColor: Color) {
        val originalPaint = g.paint
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

        g.paint = originalPaint
    }
}