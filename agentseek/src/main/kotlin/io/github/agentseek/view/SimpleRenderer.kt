package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import io.github.agentseek.common.Circle2d as Circle
import io.github.agentseek.common.Rectangle2d as Rectangle

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Graphics2D> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        val view = GameEngine.view ?: return
        val shape: Shape = when (gameObject.rigidBody.shape) {
            is Circle -> {
                val circle = gameObject.rigidBody.shape as Circle
                val converted = view.camera.toCameraCircle(circle)
                Ellipse2D.Double(
                    converted.position.x,
                    converted.position.y,
                    converted.width,
                    converted.height,
                )
            }

            is Rectangle -> {
                val rectangle2d = gameObject.rigidBody.shape as Rectangle
                val converted = view.camera.toCameraRectangle(rectangle2d)
                Rectangle2D.Double(
                    converted.position.x,
                    converted.position.y,
                    converted.width,
                    converted.height,
                )
            }

        }
        renderingContext?.render {
            it.draw(shape)
        }
    }
}