package io.github.agentseek.core

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.events.Event
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.view.EmptyRenderer
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * Main class of the model. It handles almost every entity present in the [World].
 */
class GameObject(
    /**
     * The GameObject graphical appearance
     */
    var renderer: Renderer = EmptyRenderer(),
    /**
     * The world of this GameObject
     */
    val world: World,
) {
    /**
     * Identifier for this GameObject
     */
    val id: String = world.generateId("go")
    var rigidBody: RigidBody = RigidBody.CircleRigidBody(DEFAULT_HITBOX_RADIUS, this)

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
        renderer.render(this)
    }

    /**
     * Adds a [component] to this object.
     */
    @Throws(IllegalStateException::class)
    fun addComponent(component: Component) {
        check(!components.any { component.javaClass.isInstance(it) })
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
         * The default HitBox radius of a GameObject.
         */
        const val DEFAULT_HITBOX_RADIUS = 1.5
    }
}
