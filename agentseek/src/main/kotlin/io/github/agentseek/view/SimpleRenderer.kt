package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.strokeShape
import java.awt.Graphics2D

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Graphics2D> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        renderingContext?.strokeShape(gameObject.rigidBody.shape) ?: return
    }
}