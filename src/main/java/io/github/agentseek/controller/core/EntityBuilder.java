package ryleh.controller.core;

import ryleh.common.Point2d;
import ryleh.controller.Entity;
import ryleh.model.Type;
import ryleh.model.components.AbstractComponent;
import ryleh.model.physics.HitBox;
import ryleh.view.graphics.GraphicComponent;
/**
 * A builder to make Entity constructing easier and safer.
 */
public interface EntityBuilder {

    /**
     * Sets the type of the GameObject instance.
     * 
     * @param type Type value
     * @return EntityBuilder instance.
     */
    EntityBuilder type(Type type);

    /**
     * Sets the position of a GameObject instance.
     * 
     * @param position Point2d instance.
     * @return EntityBuilder instance.
     */
    EntityBuilder position(Point2d position);

    /**
     * Sets the position of a GameObject instance.
     * 
     * @param x int value.
     * @param y int value.
     * @return EntityBuilder instance.
     */
    EntityBuilder position(int x, int y);

    /**
     * Adds an AbstarctComponent instance to a GameObject.
     * 
     * @param component AbstactComponent instance.
     * @return EntityBuilder instance.
     */
    EntityBuilder with(AbstractComponent component);

    /**
     * Sets the GraphicComponent component.
     * 
     * @param view GraphicComponent instance.
     * @return EntityBuilder instance.
     */
    EntityBuilder view(GraphicComponent view);

    /**
     * Sets the HitBox attribute.
     * 
     * @param bbox HitBox instance.
     * @return EntityBuilder instance.
     */
    EntityBuilder bbox(HitBox bbox);

    /**
     * Creates a new EntityImpl instance with graphic and object attributes.
     * 
     * @return EntityBuilder instance.
     */
    Entity build();
}
