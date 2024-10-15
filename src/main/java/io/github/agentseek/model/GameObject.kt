package ryleh.model


import io.github.agentseek.common.Point2d
import java.util.*

/**
 * An interface to handle an object of the game world.
 */
interface GameObject {
    /**
     * This method is called when the object is added to the world.
     *
     * @param world World to which this object is added.
     */
    fun onAdded(world: World?)

    /**
     * This method is called once every update. This will update every component
     * added to this object.
     *
     * @param deltaTime Time elapsed since last update.
     */
    fun onUpdate(deltaTime: Double)

    /**
     * This object's current position.
     *
     * @return Object's current position.
     */
    fun getPosition(): Point2d?

    /**
     * Sets this object's current position.
     *
     * @param position Position to be set.
     */
    fun setPosition(position: Point2d?)

    /**
     * Gets the list of components added to this object.
     *
     * @return List of components.
     */
    val components: List<Any?>?

    /**
     * Gets component of a certain class that extends AbstractComponent, or an empty
     * Optional if this object doesn't have that component.
     *
     * @param type Type of the component.
     * @return Optional of Component if contains that component, or an empty
     * Optional otherwise.
     */
    fun getComponent(type: Class<out AbstractComponent?>?): Optional<out AbstractComponent?>?

    /**
     * Adds a component to this object.
     *
     * @param component Component to be added.
     */
    fun addComponent(component: AbstractComponent?)

    /**
     * Gets this object's type.
     *
     * @return Type of this object.
     */
    fun getType(): Type?

    /**
     * Sets this object's type.
     *
     * @param type Type to be set.
     */
    fun setType(type: Type?)

    /**
     * Sets this object's hitbox.
     *
     * @param box Hitbox set.
     */
    fun setHitBox(box: HitBox?)

    /**
     * Gets this object's hitbox.
     *
     * @return Hitbox of this object.
     */
    fun getHitBox(): HitBox?
}
