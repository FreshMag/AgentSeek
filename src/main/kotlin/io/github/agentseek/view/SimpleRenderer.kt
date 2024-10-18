package io.github.agentseek.view

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.Utils.toCameraPoint
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D
import io.github.agentseek.common.Circle2d as Circle
import io.github.agentseek.common.Rectangle2d as Rectangle

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer {
    override fun render(gameObject: GameObject) {
        val shape: Shape? = when(gameObject.rigidBody.shape) {
            is Circle -> {
                val radius = (gameObject.rigidBody.shape as Circle).radius
                val upperLeftScreenPoint = GameGui.toCameraPoint(gameObject.rigidBody.shape.position)
                val lowerRight = gameObject.rigidBody.shape.position + Vector2d(radius * 2.0, radius * 2.0)
                val lowerRightScreenPoint = GameGui.toCameraPoint(lowerRight)
                val difference = lowerRightScreenPoint - upperLeftScreenPoint
                Ellipse2D.Double(
                    upperLeftScreenPoint.x,
                    upperLeftScreenPoint.y,
                    difference.x,
                    difference.y
                )
            }
            is Rectangle -> {
                val upperLeftScreenPoint = GameGui.toCameraPoint((gameObject.rigidBody.shape as Rectangle).upperLeft)
                val bottomRightScreenPoint = GameGui.toCameraPoint((gameObject.rigidBody.shape as Rectangle).lowerRight)
                val difference = bottomRightScreenPoint - upperLeftScreenPoint
                Rectangle2D.Double(
                    upperLeftScreenPoint.x,
                    upperLeftScreenPoint.y,
                    difference.x,
                    difference.y)
             }
            else -> null

        }
        shape?.let { GameGui.drawShape(it) }
    }
}