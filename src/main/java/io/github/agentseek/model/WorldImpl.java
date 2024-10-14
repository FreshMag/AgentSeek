package ryleh.model;

import java.util.ArrayList;
import java.util.List;
import ryleh.common.Point2d;
import ryleh.common.Rectangle2d;
import ryleh.common.Shape2d;
import ryleh.controller.core.GameEngine;
import ryleh.controller.events.Event;
import ryleh.controller.events.EventListener;
/**
 * An implementation of Game World. Bounds are represented by a rectangle and game objects are contained inside a list.
 */
public class WorldImpl implements World {

    private final List<GameObject> gameObjects;
    private final Rectangle2d bounds;
    private int rylehId;
    private static final int BOUNDS_WIDTH = 1407;
    private static final int BOUNDS_HEIGHT = 736;
    private static final int BOUNDS_UPPER_LEFT_X = 252;
    private static final int BOUNDS_UPPER_LEFT_Y = 172;

    private final EventListener eventListener;
    /**
     * Instantiate a World and his bounds.
     * @param eventListener Event listener for this world.
     */
    public WorldImpl(final EventListener eventListener) {
        this.eventListener = eventListener;
        this.gameObjects = new ArrayList<>();
        bounds = new Rectangle2d(BOUNDS_WIDTH, BOUNDS_HEIGHT, BOUNDS_UPPER_LEFT_X, BOUNDS_UPPER_LEFT_Y);
        GameEngine.runDebugger(() -> System.out.println(bounds.getUpperLeft() + " " + bounds.getLowerRight()));
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public String generateId(final String type) {
        rylehId++;
        return type + "RY" + this.rylehId;
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean isOutOfBounds(final Point2d position) {
        return !bounds.contains(position);
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public void addGameObject(final GameObject object) {
        gameObjects.add(object);
        object.onAdded(this);
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public Shape2d getBounds() {
        return this.bounds;
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public void removeGameObject(final GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }
    /**
     *  {@inheritDoc}
     */
    @Override
    public void notifyWorldEvent(final Event e) {
        this.eventListener.notifyEvent(e);
    }

}
