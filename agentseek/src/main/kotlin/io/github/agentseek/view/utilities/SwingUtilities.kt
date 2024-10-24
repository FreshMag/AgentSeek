package io.github.agentseek.view.utilities

import io.github.agentseek.common.Cone2d
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.geom.AffineTransform
import kotlin.math.cos
import kotlin.math.sin


object SwingUtilities {

    fun drawCone(g: Graphics2D, cone: Cone2d) {
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
}