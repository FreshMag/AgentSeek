package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillShape
import java.awt.Color
import java.awt.Graphics2D

open class SimpleRenderer(
    override val layer: Layer = Layer.GENERIC,
    val color: Color = Color.BLACK,
) : Renderer<Graphics2D> {
    private val additionalRendering = mutableListOf<(GameObject, RenderingContext<Graphics2D>?) -> Unit>()

    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        renderingContext?.fillShape(gameObject.rigidBody.shape, color) ?: return
        additionalRendering.forEach { it(gameObject, renderingContext) }
    }

    override fun attachRenderer(renderingBehavior: (GameObject, RenderingContext<Graphics2D>?) -> Unit) {
        additionalRendering.add(renderingBehavior)
    }
}