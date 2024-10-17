package io.github.agentseek.core

import io.github.agentseek.common.Point2d
import io.github.agentseek.world.World
import io.github.agentseek.components.Component
import io.github.agentseek.components.PlayerComponent
import io.github.agentseek.events.Event
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.view.Renderer
import kotlin.time.Duration

/**
 * Main class of the model. It handles almost every entity present in the [World].
 */
class GameObject(
    /**
     * The GameObject graphical appearance
     */
    var renderer: Renderer = TODO(),
    /**
     * This object's [HitBox].
     */
    val hitBox: HitBox = CircleHitBox(DEFAULT_HITBOX_RADIUS),
    /**
     * The world of this GameObject
     */
    val world: World
) {
    /**
     * Identifier for this GameObject
     */
    val id: String = world.generateId("gameObject")
    /**
     * This object's current position.
     */
    var position: Point2d = Point2d(0.0, 0.0)
        set(value) {
            hitBox.form.position = value
            field = value
        }
    /**
     * Gets the [List] of [Component]s added to this object.
     */
    var components: List<Component> = ArrayList()

    init {
        hitBox.form.position = position
    }
    /**
     * This method is called once every update. This will update every component
     * added to this object. [deltaTime] is the time elapsed since last update.
     */
    fun onUpdate(deltaTime: Duration) {
        components.forEach { it.onUpdate(deltaTime) }
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
    inline fun <reified T : Component> GameObject.hasComponent(): Boolean =
        components.find { it is T } != null

    /**
     * Returns `true` if this [GameObject] is the player.
     */
    fun GameObject.isPlayer(): Boolean = hasComponent<PlayerComponent>()

    /**
     * Notifies an event to the world of the GameObject
     */
    fun notifyEvent(event: Event) {
        world.notifyEvent(event, this)
    }

    override fun toString(): String {
        return "GameObjectImpl [id=$id]"
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

    companion object {
        /**
         * The default HitBox radius of a GameObject.
         */
        const val DEFAULT_HITBOX_RADIUS = 100
    }
}
