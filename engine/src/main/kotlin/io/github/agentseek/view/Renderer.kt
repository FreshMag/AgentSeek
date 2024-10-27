package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine

/**
 * Defines how a [GameObject] should appear on the view.
 */
interface Renderer<T> {
    /**
     * Layer where this Renderer will render
     */
    val layer: Layer

    /**
     * The only function a Renderer must implement. Defines the rendering behavior for this renderer
     */
    fun render(gameObject: GameObject, renderingContext: RenderingContext<T>?)

    /**
     * Updates the appearance of this [Renderer], associated to a [GameObject]
     */
    fun applyOnView(gameObject: GameObject) {
        render(gameObject, getRenderingContext())
    }

    /**
     * Attaches another rendering behavior to this renderer
     */
    fun attachRenderer(renderingBehavior: (GameObject, RenderingContext<T>?) -> Unit) {}

    /**
     * Utility function to make it easier to get the [RenderingContext] of the currently used view.
     * `null` if a [RenderingContext] of that type is currently unavailable.
     */
    fun getRenderingContext(): RenderingContext<T>? =
        GameEngine.view?.getRenderingContext()
}
