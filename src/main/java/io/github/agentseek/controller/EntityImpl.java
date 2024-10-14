package ryleh.controller;

import ryleh.model.GameObject;
import ryleh.view.graphics.GraphicComponent;
/**
 * A class that manages the attributes of an Entity type and implements Entity interface.
 */
public class EntityImpl implements Entity {

    private GraphicComponent view;
    private GameObject gameObject;
    /**
     * Instantiates an Entity given its graphic component and its game object.
     * @param view Graphic component of this entity. 
     * @param gameObject Game object of this entity.
     */
    public EntityImpl(final GraphicComponent view, final GameObject gameObject) {
        super();
        this.view = view;
        this.gameObject = gameObject;
    }

    /**
     * Creates an Entity whose purpose is still not clear. His view and game object
     * need to be set.
     */
    public EntityImpl() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphicComponent getView() {
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setView(final GraphicComponent view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameObject(final GameObject gameObject) {
        this.gameObject = gameObject;
    }
}
