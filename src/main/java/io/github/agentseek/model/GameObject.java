package ryleh.model;

import java.util.List;
import java.util.Optional;
import ryleh.common.Point2d;
import ryleh.model.components.AbstractComponent;
import ryleh.model.physics.HitBox;

/**
 * An interface to handle an object of the game world.
 */
public interface GameObject {
    /**
     * This method is called when the object is added to the world.
     * 
     * @param world World to which this object is added.
     */
    void onAdded(World world);

    /**
     * This method is called once every update. This will update every component
     * added to this object.
     * 
     * @param deltaTime Time elapsed since last update.
     */
    void onUpdate(double deltaTime);

    /**
     * This object's current position.
     * 
     * @return Object's current position.
     */
    Point2d getPosition();

    /**
     * Sets this object's current position.
     * 
     * @param position Position to be set.
     */
    void setPosition(Point2d position);

    /**
     * Gets the list of components added to this object.
     * 
     * @return List of components.
     */
    List<AbstractComponent> getComponents();

    /**
     * Gets component of a certain class that extends AbstractComponent, or an empty
     * Optional if this object doesn't have that component.
     * 
     * @param type Type of the component.
     * @return Optional of Component if contains that component, or an empty
     *         Optional otherwise.
     */
    Optional<? extends AbstractComponent> getComponent(Class<? extends AbstractComponent> type);

    /**
     * Adds a component to this object.
     * 
     * @param component Component to be added.
     */
    void addComponent(AbstractComponent component);

    /**
     * Gets this object's type.
     * 
     * @return Type of this object.
     */
    Type getType();

    /**
     * Sets this object's type.
     * 
     * @param type Type to be set.
     */
    void setType(Type type);

    /**
     * Sets this object's hitbox.
     * 
     * @param box Hitbox set.
     */
    void setHitBox(HitBox box);

    /**
     * Gets this object's hitbox.
     * 
     * @return Hitbox of this object.
     */
    HitBox getHitBox();
}
