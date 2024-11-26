package io.github.agentseek.view

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.FastEntities.allDirections
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.fillCircle
import io.github.agentseek.view.utilities.Rendering.fillRectangle
import java.awt.Graphics2D

/**
 * A renderer for the camera.
 */
class CameraRenderer(override val layer: Layer = Config.Rendering.defaultLayer) : SimpleRenderer(layer) {
    /**
     * Directions of the camera.
     */
    private val directions = allDirections().map { it * 0.5 }.map { it.rotateDegrees(45.0) }

    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        super.render(gameObject, renderingContext)
        val rectangle = Rectangle2d(Config.Agents.cameraRectangleSize, Config.Agents.cameraRectangleSize)
        rectangle.center = gameObject.center()
        renderingContext?.fillRectangle(rectangle, Config.Agents.cameraColor)
        directions.forEach { direction ->
            renderingContext?.fillCircle(
                Circle2d(Config.Agents.cameraCirclesRadius).also { it.center = gameObject.center() + direction },
                Config.Agents.cameraColor
            )
        }
    }
}