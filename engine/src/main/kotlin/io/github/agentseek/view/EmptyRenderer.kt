package io.github.agentseek.view

import io.github.agentseek.core.GameObject

class EmptyRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Nothing> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Nothing>?) {}
}