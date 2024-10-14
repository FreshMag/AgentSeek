package ryleh.model;

import java.util.List;

import ryleh.common.Point2d;
import ryleh.common.Shape2d;
import ryleh.controller.events.Event;
/**
 * Game's world. The World represents the domain of the game, containing all game objects and the bounds where the objects
 * can move. 
 */
public interface World {
    /**
     * Gets the list of game objects added to this world.
     * @return List of GameObjects.
     */
    List<GameObject> getGameObjects();
    /**
     * Generate a basic Id for each object that has a reference to this world.
     * @param type Type of the object added.
     * @return An id for the objects.
     */
    String generateId(String type);
    /**
     * Checks if a certain position is out of bounds.
     * @param position Position to check.
     * @return True if that position is out of bounds.
     */
    boolean isOutOfBounds(Point2d position);
    /**
     * Add an object to this world.
     * @param object GameObject to be added.
     */
    void addGameObject(GameObject object);
    /**
     * Return a two-dimensional shape that represents world bounds.
     * @return A two-dimensional shape.
     */
    Shape2d getBounds();
    /**
     * Remove a game object from this world. 
     * @param gameObject Game object to be removed.
     */
    void removeGameObject(GameObject gameObject);
    /**
     * Notify an event to the EventListener.
     * @param event Event to be notified.
     */
    void notifyWorldEvent(Event event);
}
