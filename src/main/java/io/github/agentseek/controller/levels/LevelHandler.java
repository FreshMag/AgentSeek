package ryleh.controller.levels;

import java.util.Collection;

import ryleh.common.Pair;
import ryleh.common.Point2d;
import ryleh.controller.Entity;
/**
 * This interface determines how to handle a level implementation.
 */
public interface LevelHandler {

    /**
     * 
     * @return The number of rooms currently visited.
     */
    int getRoomsCount();

    /**
     * Generates a new Level, clearing the previous entities from the list.
     */
    void generateNewLevel();

    /**
     * Converts the coordinates taken from the grid of spawn points into world
     * coordinates.
     * 
     * @param spawnPoint
     * @return P2d representing position converted into world's coordinates
     */
    Point2d getPosition(Pair<Integer, Integer> spawnPoint);

    /**
     * @return player's spawn point
     */
    Pair<Integer, Integer> getPlayerSpawn();

    /**
     * Decreases enemies. If nEnemies equals 0, then we are at the end of the level.
     */
    void decreaseEnemies();

    /**
     * 
     * @return the list of entities inside the current level.
     */
    Collection<Entity> getEntities();

    /**
     * Sets current level.
     * 
     * @param level Level to be set.
     */
    void setLevel(int level);

}
