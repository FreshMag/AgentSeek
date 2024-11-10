package io.github.agentseek.view

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.FastEntities.allDirections
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillCircle
import io.github.agentseek.view.utilities.Rendering.fillRectangle
import java.awt.Color
import java.awt.Graphics2D

class CameraRenderer(override val layer: Layer = Layer.GENERIC) : SimpleRenderer(layer) {

    private val directions = allDirections().map { it * 0.5 }.map { it.rotateDegrees(45.0) }

    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        super.render(gameObject, renderingContext)
        val rectangle = Rectangle2d(1.0, 1.0)
        rectangle.center = gameObject.center()
        renderingContext?.fillRectangle(rectangle, Color.DARK_GRAY)
        directions.forEach { direction ->
            renderingContext?.fillCircle(
                Circle2d(0.5).also { it.center = gameObject.center() + direction }, Color.DARK_GRAY
            )
        }
    }
}