package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillShape
import io.github.agentseek.view.utilities.Rendering.strokeShape
import java.awt.Color
import java.awt.Graphics2D

open class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Graphics2D> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        renderingContext?.fillShape(gameObject.rigidBody.shape, Color.BLACK) ?: return
    }
}