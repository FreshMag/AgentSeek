package io.github.agentseek.view.swing

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.Layer
import io.github.agentseek.view.Renderer
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import io.github.agentseek.common.Circle2d as Circle
import io.github.agentseek.common.Rectangle2d as Rectangle

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer {
    override fun render(gameObject: GameObject) {
        val view = GameEngine.view ?: return
        val shape: Shape? = when(gameObject.rigidBody.shape) {
            is Circle -> {
                val circle = gameObject.rigidBody.shape as Circle
                val radius = circle.radius
                val upperLeftScreenPoint = view.camera.toCameraPoint(circle.center - Vector2d(radius, radius))
                val lowerRightScreenPoint = view.camera.toCameraPoint(circle.center + Vector2d(radius, radius))
                val difference = lowerRightScreenPoint - upperLeftScreenPoint
                Ellipse2D.Double(
                    upperLeftScreenPoint.x,
                    upperLeftScreenPoint.y,
                    difference.x,
                    difference.y
                )
            }
            is Rectangle -> {
                val rectangle2d = gameObject.rigidBody.shape as Rectangle
                val upperLeftScreenPoint = view.camera.toCameraPoint(rectangle2d.upperLeft)
                val bottomRightScreenPoint = view.camera.toCameraPoint(rectangle2d.lowerRight)
                val difference = bottomRightScreenPoint - upperLeftScreenPoint
                Rectangle2D.Double(
                    upperLeftScreenPoint.x,
                    upperLeftScreenPoint.y,
                    difference.x,
                    difference.y)
             }
            else -> null

        }
        shape?.let { view.draw(it) }
    }
}