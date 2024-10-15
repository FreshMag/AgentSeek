package io.github.agentseek.model


import io.github.agentseek.common.Point2d
import io.github.agentseek.model.components.Component
import io.github.agentseek.model.physics.HitBox

/**
 * An interface to handle an object of the game world.
 */
interface GameObject {

    /**
     * This object's current position.
     */
    var position: Point2d

    /**
     * Gets the [List] of [Component]s added to this object.
     */
    var components: List<Component>

    /**
     * This object's [Type].
     */
    val type: Type

    /**
     * This object's [HitBox].
     */
    val hitBox: HitBox

    /**
     * This method is called when the object is added to the [world].
     */
    fun onAdded(world: World)

    /**
     * This method is called once every update. This will update every component
     * added to this object. [deltaTime] is the time elapsed since last update.
     */
    fun onUpdate(deltaTime: Double)


    /**
     * Adds a [component] to this object.
     */
    fun addComponent(component: Component)
}

/**
 * Gets component of a certain class that extends [Component], or `null` if this object doesn't have that
 * type of component.
 */
inline fun <reified T : Component> GameObject.getComponent(): T? =
    components.find { it is T } as? T

/**
 * Returns `true` if this [GameObject] is the player.
 */
fun GameObject.isPlayer(): Boolean = type == Type.PLAYER