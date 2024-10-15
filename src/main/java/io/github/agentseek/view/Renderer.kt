package io.github.agentseek.view

import io.github.agentseek.controller.GameObject

/**
 * Defines how a [GameObject] should appear on the view.
 */
interface Renderer {
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
     * A method that returns the zIndex of a GraphicComponent.
     *
     * @return The zIndex of a GraphicComponent.
     */
    /**
     * The method that sets the zIndex of a graphicCOmponent in the view,
     * which is used to define the right order to render each GraphicComponent.
     *
     * @param zIndex the value of the zIndex that needs to be set to a
     * graphicComponent.
     */
    // var zindex: Int

    /**
     * A Method that returns the current texture of the graphicComponent.
     * @return The Texture.
     */
    // val texture: Textures?
}
