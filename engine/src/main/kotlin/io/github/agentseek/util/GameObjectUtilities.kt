package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.Layer
import io.github.agentseek.view.Renderer
import io.github.agentseek.view.RenderingContext

object GameObjectUtilities {

    /**
     * Returns all the other game objects except for this one
     */
    fun GameObject.otherGameObjects(): Iterable<GameObject> =
        world.gameObjects.filterNot { it.id == this.id }

    /**
     * Returns the center of the rigid body of this object
     */
    fun GameObject.center(): Point2d =
        rigidBody.shape.center

    /**
     * Attaches another [Renderer] to this GameObject, allowing extensible rendering behavior
     */
    fun <T> GameObject.attachRenderer(renderingBehavior: (GameObject, RenderingContext<T>?) -> Unit) {
        val originalRenderer = this.renderer
        this.renderer = object : Renderer<T> {
            override val layer: Layer
                get() = originalRenderer.layer

            override fun render(gameObject: GameObject, renderingContext: RenderingContext<T>?) {
                renderingBehavior(gameObject, renderingContext)
                originalRenderer.applyOnView(gameObject)
            }
        }
    }

}
