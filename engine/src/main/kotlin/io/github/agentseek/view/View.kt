package io.github.agentseek.view

interface View {
    val screenWidth: Int
    val screenHeight: Int

    /**
     * Camera used to render world objects into the screen
     */
    val camera: Camera

    /**
     * Rendering logic of this view. Called every frame update.
     */
    fun render()

    /**
     * Draws something on the View. Can be anything depending on its implementation.
     */
    fun draw(obj: Any)
}