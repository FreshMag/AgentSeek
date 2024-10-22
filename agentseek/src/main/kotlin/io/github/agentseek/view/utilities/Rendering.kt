package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Cone2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.view.RenderingContext
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

object Rendering {

    /**
     * Strokes a simple black circle starting from a [Circle2d]
     */
    fun RenderingContext<Graphics2D>.strokeCircle(circle2d: Circle2d) {
        val converted = camera.toCameraCircle(circle2d)
        render {
            it.draw(
                Ellipse2D.Double(
                    converted.position.x,
                    converted.position.y,
                    converted.width,
                    converted.height,
                )
            )
        }
    }

    /**
     * Strokes a simple black rectangle starting from a [Rectangle2d]
     */
    fun RenderingContext<Graphics2D>.strokeRectangle(rectangle2d: Rectangle2d) {
        val converted = camera.toCameraRectangle(rectangle2d)
        render {
            it.draw(
                Rectangle2D.Double(
                    converted.position.x,
                    converted.position.y,
                    converted.width,
                    converted.height,
                )
            )
        }
    }

    /**
     * Strokes a simple black cone starting from a [Cone2d]
     */
    fun RenderingContext<Graphics2D>.strokeCone(cone2d: Cone2d) {
        val cone = camera.toCameraCone(cone2d)
        render {
            SwingUtilities.drawCone(it, cone)
        }
    }

    /**
     * Strokes a simple black shape starting from a [Shape2d]
     */
    fun RenderingContext<Graphics2D>.strokeShape(shape: Shape2d) =
        render {
            when (shape) {
                is Circle2d -> strokeCircle(shape)
                is Rectangle2d -> strokeRectangle(shape)
                is Cone2d -> strokeCone(shape)
            }
        }
}
