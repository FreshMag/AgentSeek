package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.view.RenderingContext
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

object Rendering {

    /**
     * Strokes a simple black circle starting from a [Circle2d]
     */
    fun RenderingContext<Graphics2D>.strokeCircle(circle2d: Circle2d, color: Color) {
        val circle = camera.toCameraCircle(circle2d)
        render {
            val previousColor = it.color
            it.color = color
            it.draw(
                Ellipse2D.Double(
                    circle.position.x,
                    circle.position.y,
                    circle.width,
                    circle.height,
                )
            )
            it.color = previousColor
        }
    }

    /**
     * Strokes a simple black rectangle starting from a [Rectangle2d]
     */
    fun RenderingContext<Graphics2D>.strokeRectangle(rectangle2d: Rectangle2d, color: Color) {
        val rectangle = camera.toCameraRectangle(rectangle2d)
        render {
            val previousColor = it.color
            it.color = color
            it.draw(
                Rectangle2D.Double(
                    rectangle.position.x,
                    rectangle.position.y,
                    rectangle.width,
                    rectangle.height,
                )
            )
            it.color = previousColor
        }
    }

    /**
     * Strokes a simple black cone starting from a [Cone2d]
     */
    fun RenderingContext<Graphics2D>.strokeCone(cone2d: Cone2d, color: Color) {
        val cone = camera.toCameraCone(cone2d)
        render {
            SwingUtilities.strokeCone(it, cone, color)
        }
    }

    /**
     * Strokes a simple black shape starting from a [Shape2d]
     */
    fun RenderingContext<Graphics2D>.strokeShape(shape: Shape2d, color: Color) =
        render {
            when (shape) {
                is Circle2d -> strokeCircle(shape, color)
                is Rectangle2d -> strokeRectangle(shape, color)
                is Cone2d -> strokeCone(shape, color)
            }
        }

    /**
     * Fills a cone with a gradient
     */
    fun RenderingContext<Graphics2D>.fillGradientCone(cone2d: Cone2d, startColor: Color, endColor: Color) {
        val cone = camera.toCameraCone(cone2d)
        render {
            SwingUtilities.fillConeWithGradient(it, cone, startColor, endColor)
        }
    }

    /**
     * Fills a cone with a gradient
     */
    fun RenderingContext<Graphics2D>.fillGradientCircle(circle2d: Circle2d, startColor: Color, endColor: Color) {
        val circle = camera.toCameraCircle(circle2d)
        render {
            SwingUtilities.fillCircleWithGradient(it, circle, startColor, endColor)
        }
    }
}
