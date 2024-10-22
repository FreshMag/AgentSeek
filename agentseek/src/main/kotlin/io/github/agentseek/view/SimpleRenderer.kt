package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.view.utilities.Rendering
import java.awt.Graphics2D

class SimpleRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Graphics2D> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        val view = GameEngine.view ?: return
        renderingContext?.render(Rendering.strokeShape(gameObject.rigidBody.shape, view))
    }
}