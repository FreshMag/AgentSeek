package io.github.agentseek.core


import io.github.agentseek.common.Point2d
import io.github.agentseek.world.World
import io.github.agentseek.components.Component
import io.github.agentseek.components.PlayerComponent
import io.github.agentseek.events.Event
import io.github.agentseek.physics.HitBox
import io.github.agentseek.view.Renderer
import kotlin.time.Duration

/**
 * An interface to handle an object of the game world.
 */
interface GameObject {
    /**
     * The world of this GameObject
     */
    val world: World
    /**
     * This object's current position.
     */
    var position: Point2d

    /**
     * Gets the [List] of [Component]s added to this object.
     */
    var components: List<Component>

    /**
     * This object's [HitBox].
     */
    val hitBox: HitBox

    /**
     * The GameObject graphical appearance
     */
    var renderer: Renderer

    /**
     * This method is called once every update. This will update every component
     * added to this object. [deltaTime] is the time elapsed since last update.
     */
    fun onUpdate(deltaTime: Duration)


    /**
     * Adds a [component] to this object.
     */
    fun addComponent(component: Component)

    /**
     * Spawns a new [gameObject] in the world.
     */
    fun spawn(gameObject: GameObject)

    /**
     * Deletes this [GameObject] from the [World]
     */
    fun delete()

    /**
     * Notifies an event to the world of the GameObject
     */
    fun notifyEvent(event: Event) {
        world?.notifyEvent(event, this)
    }
}

/**
 * Gets component of a certain class that extends [Component], or `null` if this object doesn't have that
 * type of component.
 */
inline fun <reified T : Component> GameObject.getComponent(): T? =
    components.find { it is T } as? T

inline fun <reified T : Component> GameObject.hasComponent(): Boolean =
    components.find { it is T } != null

/**
 * Returns `true` if this [GameObject] is the player.
 */
fun GameObject.isPlayer(): Boolean = hasComponent<PlayerComponent>()
