package ryleh.controller.core;

import ryleh.common.Point2d;
import ryleh.controller.Entity;
import ryleh.controller.EntityImpl;
import ryleh.model.GameObject;
import ryleh.model.GameObjectImpl;
import ryleh.model.Type;
import ryleh.model.components.AbstractComponent;
import ryleh.model.physics.HitBox;
import ryleh.view.graphics.GraphicComponent;
/**
 * Main Implementation of Entity Builder.
 */
public class EntityBuilderImpl implements EntityBuilder {
    private final GameObject object;
    private GraphicComponent graphic;

    /**
     * Constructor method.
     */
    public EntityBuilderImpl() {
        object = new GameObjectImpl();
        graphic = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder type(final Type type) {
        object.setType(type);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder position(final Point2d position) {
        object.setPosition(position);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder position(final int x, final int y) {
        object.setPosition(new Point2d(x, y));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder with(final AbstractComponent component) {
        try {
            object.addComponent(component);
        } catch (IllegalStateException e) {
            GameEngine.runDebugger(
                    () -> System.out.println("Something went wrong....Exception when trying to add component\n"));
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder view(final GraphicComponent view) {
        graphic = view;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityBuilder bbox(final HitBox bbox) {
        object.setHitBox(bbox);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity build() {
        return new EntityImpl(graphic, object);
    }
}
