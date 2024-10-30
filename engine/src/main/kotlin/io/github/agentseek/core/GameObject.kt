package io.github.agentseek.core

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.components.Requires
import io.github.agentseek.events.Event
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.serialization.GameObjectSerializer
import io.github.agentseek.view.EmptyRenderer
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World
import kotlin.reflect.full.findAnnotation
import kotlin.time.Duration

/**
 * Main class of the model. It handles almost every entity present in the [World].
 */
class GameObject(
    /**
     * The GameObject graphical appearance
     */
    var renderer: Renderer<*> = EmptyRenderer(),
    /**
     * The world of this GameObject
     */
    val world: World,
) {
    /**
     * Identifier for this GameObject
     */
    val id: String = world.generateId("go")
    var rigidBody: RigidBody = RigidBody.RectangleRigidBody(this, DEFAULT_SIZE, DEFAULT_SIZE)

    /**
     * An optional, recognizable name for this GameObject.
     *
     * Note: objects with the same name will be saved as a **single** file from a [Scene]
     */
    var name: String = ""

    /**
     * This object's current position.
     */
    var position: Point2d = Point2d(0.0, 0.0)
        set(value) {
            rigidBody.shape.position = value
            field = value
        }
        get() = rigidBody.shape.position

    /**
     * Gets the [List] of [Component]s added to this object.
     */
    var components: List<Component> = ArrayList()

    /**
     * This method is called once every update. This will update every component
     * added to this object. [deltaTime] is the time elapsed since last update.
     */
    internal fun onUpdate(deltaTime: Duration) {
        components.forEach { it.onUpdate(deltaTime) }
        rigidBody.onUpdate(deltaTime)
        renderer.applyOnView(this)
    }

    /**
     * Adds a [component] to this object. Throws an [IllegalStateException] if this GameObject already has that
     * component or if some internal requirements of the component to be added are not satisfied.
     */
    @Throws(IllegalStateException::class)
    fun addComponent(component: Component) {
        check(!components.any { component.javaClass.isInstance(it) })
        val componentClass = component::class
        val requiredComponents = componentClass.findAnnotation<Requires>()?.required
        if (requiredComponents != null) {
            val missingComponents = requiredComponents.filter { required ->
                components.none { required.isInstance(it) }
            }
            if (missingComponents.isNotEmpty()) {
                throw IllegalStateException(
                    "Cannot add ${componentClass.simpleName} because it requires " +
                            requiredComponents.joinToString(", ") { it.simpleName.toString() }
                )
            }
        }
        components += component
        component.init()
    }

    /**
     * Removes a [component] from this object.
     */
    fun removeComponent(component: Component) {
        component.onRemoved()
        components -= component
    }

    /**
     * Spawns a new [gameObject] in the world.
     */
    fun spawn(gameObject: GameObject) {
        world.addGameObject(gameObject)
    }

    /**
     * Deletes this [GameObject] from the [World]
     */
    fun delete() {
        components.forEach { it.onRemoved() }
        world.removeGameObject(this)
    }

    /**
     * Gets component of a certain class that extends [Component], or `null` if this object doesn't have that
     * type of component.
     */
    inline fun <reified T : Component> getComponent(): T? =
        components.find { it is T } as? T

    /**
     * Returns `true` if this [GameObject] has that a [Component] of class [T]
     */
    inline fun <reified T : Component> hasComponent(): Boolean =
        components.find { it is T } != null

    /**
     * Returns `true` if this [GameObject] is the player.
     */
    fun isPlayer(): Boolean = TODO()

    /**
     * Notifies an event to the world of the GameObject
     */
    fun notifyEvent(event: Event) {
        world.notifyEvent(event, this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameObject

        if (id != other.id) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode() ?: 0
        result = 31 * result + position.hashCode()
        result = 31 * result + components.hashCode()
        return result
    }

    override fun toString(): String {
        return "GameObject(id='$id', components=$components, \"position=$position, renderer=$renderer, hitBox=$rigidBody)"
    }

    companion object {
        /**
         * The default RigidBody size of a GameObject. Note that this can have different meaning: for example for
         * a [io.github.agentseek.physics.RigidBody.CircleRigidBody] can be its radius, instead for a
         * [io.github.agentseek.physics.RigidBody.RectangleRigidBody] its width and height.
         */
        const val DEFAULT_SIZE = 1.5
    }
}
