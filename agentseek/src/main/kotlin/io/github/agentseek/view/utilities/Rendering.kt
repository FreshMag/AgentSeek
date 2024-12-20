package io.github.agentseek.view.utilities

import io.github.agentseek.common.*
import io.github.agentseek.view.RenderingContext
import io.github.agentseek.view.gui.GameGui
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

/**
 * Utility object for rendering shapes.
 */
object Rendering {

    /**
     * Draws a [vector2d] from a given [position] with a specified [color].
     */
    fun RenderingContext<Graphics2D>.drawVector(position: Point2d, vector2d: Vector2d, color: Color) {
        render {
            val previousColor = it.color
            it.color = color
            val p1 = camera.toCameraPoint(position)
            val p2 = camera.toCameraPoint(position + vector2d)
            it.drawLine(
                p1.x.toInt(),
                p1.y.toInt(),
                p2.x.toInt(),
                p2.y.toInt()
            )
            it.color = previousColor
        }
    }

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
     * Strokes a simple shape starting from a [Shape2d] with [color]
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
     * Fills a simple shape starting from a [Shape2d] with [color]
     */
    fun RenderingContext<Graphics2D>.fillShape(shape: Shape2d, color: Color) {
        render {
            when (shape) {
                is Circle2d -> fillCircle(shape, color)
                is Rectangle2d -> fillRectangle(shape, color)
                is Cone2d -> fillCone(shape, color)
            }
        }
    }

    /**
     * Fills a circle with [color]
     */
    fun RenderingContext<Graphics2D>.fillCircle(circle2d: Circle2d, color: Color) {
        val circle = camera.toCameraCircle(circle2d)
        render {
            SwingUtilities.fillCircleWithColor(it, circle, color)
        }
    }

    /**
     * Fills a rectangle with [color]
     */
    fun RenderingContext<Graphics2D>.fillRectangle(rectangle2d: Rectangle2d, color: Color) {
        val rectangle = camera.toCameraRectangle(rectangle2d)
        render {
            SwingUtilities.fillRectangleWithColor(it, rectangle, color)
        }
    }

    /**
     * Fills a cone with [color]
     */
    fun RenderingContext<Graphics2D>.fillCone(cone2d: Cone2d, color: Color) {
        val cone = camera.toCameraCone(cone2d)
        render {
            SwingUtilities.fillConeWithColor(it, cone, color)
        }
    }

    /**
     * Fills a simple shape with a gradient starting from [startColor] and [endColor]
     */
    fun RenderingContext<Graphics2D>.fillShapeWithGradient(shape: Shape2d, startColor: Color, endColor: Color) {
        render {
            when (shape) {
                is Circle2d -> fillGradientCircle(shape, startColor, endColor)
                is Rectangle2d -> fillGradientRectangle(shape, startColor, endColor)
                is Cone2d -> fillGradientCone(shape, startColor, endColor)
            }
        }
    }

    /**
     * Fills a circle with a gradient
     */
    fun RenderingContext<Graphics2D>.fillGradientCircle(circle2d: Circle2d, startColor: Color, endColor: Color) {
        val circle = camera.toCameraCircle(circle2d)
        render {
            SwingUtilities.fillCircleWithGradient(it, circle, startColor, endColor)
        }
    }

    /**
     * Fills a rectangle with a gradient
     */
    fun RenderingContext<Graphics2D>.fillGradientRectangle(rectangle2d: Rectangle2d,
                                                           startColor: Color,
                                                           endColor: Color) {
        val rectangle = camera.toCameraRectangle(rectangle2d)
        render {
            SwingUtilities.fillRectangleWithGradient(it, rectangle, startColor, endColor)
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
     * Fills the screen with a [title] and [subtitle], using the specified [backgroundColor] and [textColor].
     */
    fun RenderingContext<Graphics2D>.fillScreenWithTitleAndSubtitle(
        backgroundColor: Color,
        title: String,
        subtitle: String,
        textColor: Color,
    ) {
        render { g2d ->
            val width = GameGui.screenWidth
            val height = GameGui.screenHeight
            val titleFont = g2d.font.deriveFont(64f)
            g2d.font = titleFont
            val titleMetrics = g2d.fontMetrics
            val titleWidth = titleMetrics.stringWidth(title)
            val titleHeight = titleMetrics.height

            // Draw background rectangle
            g2d.color = backgroundColor
            g2d.fillRect(0, 0, width, height)

            // Draw text
            g2d.color = textColor
            g2d.drawString(title, (width - titleWidth) / 2, (height + titleHeight) / 2 - titleMetrics.descent - 40)
            val subtitleFont = g2d.font.deriveFont(24f)
            g2d.font = subtitleFont
            val subtitleMetrics = g2d.fontMetrics
            val subtitleWidth = subtitleMetrics.stringWidth(subtitle)
            val subtitleHeight = subtitleMetrics.height
            g2d.drawString(
                subtitle,
                (width - subtitleWidth) / 2,
                (height + subtitleHeight) / 2 - subtitleMetrics.descent + 40
            )
        }
    }
}
