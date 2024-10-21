package io.github.agentseek.view

import io.github.agentseek.core.GameObject

/**
 * Defines how a [GameObject] should appear on the view.
 */
interface Renderer {
    /**
     * Layer where this Renderer will render
     */
    val layer: Layer
    /**
     * Updates the appearance of this [Renderer], associated to a [GameObject]
     */
    fun render(gameObject: GameObject)
}
