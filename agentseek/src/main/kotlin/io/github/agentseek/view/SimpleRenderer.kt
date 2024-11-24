package io.github.agentseek.view

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillShape
import java.awt.Graphics2D

open class SimpleRenderer(override val layer: Layer = Config.Rendering.defaultLayer) : Renderer<Graphics2D> {
    private val additionalRendering = mutableListOf<(GameObject, RenderingContext<Graphics2D>?) -> Unit>()

    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Graphics2D>?) {
        renderingContext?.fillShape(gameObject.rigidBody.shape, Config.Rendering.defaultColor) ?: return
        additionalRendering.forEach { it(gameObject, renderingContext) }
    }

    override fun attachRenderer(renderingBehavior: (GameObject, RenderingContext<Graphics2D>?) -> Unit) {
        additionalRendering.add(renderingBehavior)
    }
}