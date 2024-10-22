package io.github.agentseek.view.utilities

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.view.View
import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

object Rendering {

    /**
     * Returns a rendering behavior that strokes a simple black circle starting from a [Circle2d], using a camera
     * conversion specified by the given [view]
     */
    fun strokeCircle(circle2d: Circle2d, view: View): (Graphics2D) -> Unit {
        val converted = view.camera.toCameraCircle(circle2d)
        return {
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
     * Returns a rendering behavior that strokes a simple black rectangle starting from a [Rectangle2d], using a camera
     * conversion specified by the given [view]
     */
    fun strokeRectangle(rectangle2d: Rectangle2d, view: View): (Graphics2D) -> Unit {
        val converted = view.camera.toCameraRectangle(rectangle2d)
        return {
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
     * Returns a rendering behavior that strokes a simple black shape starting from a [Shape2d], using a camera
     * conversion specified by the given [view]
     */
    fun strokeShape(shape: Shape2d, view: View): (Graphics2D) -> Unit =
        when (shape) {
            is Circle2d -> strokeCircle(shape, view)
            is Rectangle2d -> strokeRectangle(shape, view)
        }

}