package io.github.agentseek.view

/**
 * Represents a view that can render the game world. The rendering events are handled through the use of [Renderer]s.
 */
interface View {
    /**
     * Width of the screen in pixels
     */
    val screenWidth: Int

    /**
     * Height of the screen in pixels
     */
    val screenHeight: Int

    /**
     * Camera used to render world objects into the screen
     */
    val camera: Camera

    /**
     * Gets the context for rendering on the view.
     */
    fun <T> getRenderingContext(): RenderingContext<T>?

    /**
     * Rendering logic of this view. Called every frame update.
     */
    fun render()

    /**
     * Returns the default renderer for this view.
     */
    fun defaultRenderer(): Renderer<*>

}