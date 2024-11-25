package io.github.agentseek.view

import java.util.*

/**
 * Rendering context that holds the camera and the effects to be rendered.
 * @param T the type of the object to be rendered
 * @property camera the camera to be used for rendering
 */
class RenderingContext<T>(val camera: Camera) {
    /**
     * The list of effects to be rendered.
     */
    private val effects: MutableList<(T) -> Unit> = Collections.synchronizedList(mutableListOf())

    /**
     * Clear the effects list and return the list of effects not yet rendered since the last rendering event.
     */
    fun sinkToBuffer(): List<(T) -> Unit> = effects.toList().also { clearEvents() }

    /**
     * Clear the effects list.
     */
    fun clearEvents() = effects.clear()

    /**
     * Add an effect to the list of effects to be rendered.
     * @param effect the effect to be rendered
     */
    fun render(effect: (T) -> Unit) = effects.add(effect)

}
