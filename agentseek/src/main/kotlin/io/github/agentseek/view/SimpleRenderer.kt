package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.gui.GameGui
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import io.github.agentseek.common.Circle2d as Circle
import io.github.agentseek.common.Rectangle2d as Rectangle

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer {
    override fun render(gameObject: GameObject) {
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
        view.draw(GameGui.RenderingEvent {
            it.draw(shape)
        })
    }
}