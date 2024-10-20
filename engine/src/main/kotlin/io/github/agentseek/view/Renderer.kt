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

    /**
     * The method that specifies the actions to be done when a GraphicComponent is
     * added to the View.
     *
     * @param scene The scene in which this graphicComponent is to be added.
     */
    // fun onAdded(scene: Scene?)

    /**
     * @return The node related to the GraphicComponent.
     */
    // val node: Rectangle?

    /**
     * The method that specifies the actions to be done when a GraphicComponent
     * needs to be removed from the view.
     *
     * @param event The event that needs to be handled when the GraphicComponent is
     * to be removed from the view.
     */
    // fun onRemoved(event: EventHandler<ActionEvent?>?)


    /**
     * A Method that returns the current texture of the graphicComponent.
     * @return The Texture.
     */
    // val texture: Textures?
}
