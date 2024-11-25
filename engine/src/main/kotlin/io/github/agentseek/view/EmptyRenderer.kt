package io.github.agentseek.view

import io.github.agentseek.core.GameObject

/**
 * A renderer that does nothing. Useful for testing purposes or for invisible GameObjects.
 */
class EmptyRenderer(override val layer: Layer = Layer.GENERIC) : Renderer<Nothing> {
    override fun render(gameObject: GameObject, renderingContext: RenderingContext<Nothing>?) {}
}