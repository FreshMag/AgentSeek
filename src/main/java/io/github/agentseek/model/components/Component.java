package ryleh.model.components;

import ryleh.model.GameObject;
/**
 * A Component can be added to a GameObject to specify some sort of additional behavior and to distinguish one game object 
 * from another.
 */
public interface Component {
    /**
     * This method is called when this component is added to a GameObject.
     * @param object Object to which this component is added.
     */
    void onAdded(GameObject object);
    /**
     * This method is called once every update of GameState.
     * @param deltaTime Time elapsed since last update.
     */
    void onUpdate(double deltaTime);

}
