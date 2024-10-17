package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

class EmptyRenderer(override val layer: Layer = Layer.GENERIC) : Renderer {
    override fun render(gameObject: GameObject) {
        GameGui.list.addAll(mutableListOf(Rectangle2D.Float(0f, 0f, 10f, 10f), Ellipse2D.Float(10f, 10f, 10f, 10f)))
    }
}