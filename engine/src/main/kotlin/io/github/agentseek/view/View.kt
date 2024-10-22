package io.github.agentseek.view

interface View {
    val screenWidth: Int
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
     * Returns a default renderer
     */
    fun defaultRenderer(): Renderer<*>



}