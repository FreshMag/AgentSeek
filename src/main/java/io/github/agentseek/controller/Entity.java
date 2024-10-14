package ryleh.controller;

import ryleh.model.GameObject;
import ryleh.view.graphics.GraphicComponent;
/**
 * An interface to define an Entity type.
 */
public interface Entity {
    /**
     * Gets the View of this entity.
     * 
     * @return The View of this entity.
     */
    GraphicComponent getView();

    /**
     * Sets the View of this entity.
     * 
     * @param view View to be set.
     */
    void setView(GraphicComponent view);

    /**
     * Gets the GameObject of this entity.
     * 
     * @return The GameObject of this entity.
     */
    GameObject getGameObject();

    /**
     * Sets the GameObject of this entity.
     * 
     * @param gameObject GameObject to be set.
     */
    void setGameObject(GameObject gameObject);
}
