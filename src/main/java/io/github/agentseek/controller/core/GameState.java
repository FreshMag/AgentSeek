package ryleh.controller.core;

import java.util.List;
import java.util.Optional;

import ryleh.controller.Entity;
import ryleh.controller.levels.LevelHandler;
import ryleh.model.Type;
import ryleh.model.World;
import ryleh.view.ViewHandlerImpl;
/**
 * This interface represents the state of the game and generates new levels based on a LevelHandler.
 */
public interface GameState {

    /**
     * @return a reference to current player.
     */
    Entity getPlayer();

    /**
     * Add an Entity to the current GameState.
     * 
     * @param entity Entity to be added.
     */
    void addEntity(Entity entity);

    /**
     * Generates a new Level and spawns all entities inside it, player too.
     */
    void generateNewLevel();

    /**
     * Removes an Entity to current GameState.
     * 
     * @param entity Entity to be removed.
     */
    void removeEntity(Entity entity);

    /**
     * Updates current GameState of a certain amount of time elapsed.
     * 
     * @param dt Delta time, time elapsed after last update.
     */
    void updateState(double dt);

    /**
     * Checks if game is over and if player has won or lost.
     * 
     * @return True if the game is over.
     */
    boolean isGameOver();

    /**
     * Sets flags that indicates that the game is over.
     * 
     * @param victory True if player has won, false if player has lost.
     */
    void callGameOver(boolean victory);

    /**
     * Gets the object that is handling the view.
     * 
     * @return The handler of the view.
     */
    ViewHandlerImpl getView();

    /**
     * Gets the object that is handling the Levels System.
     * 
     * @return The handler of the levels system.
     */
    LevelHandler getLevelHandler();

    /**
     * Gets an Entity whose type corresponds to the type given in input, taken from
     * the current active entities.
     * 
     * @param type Type of the Entity.
     * @return An Optional of Entity if an Entity with the determined type was
     *         present, or an empty Optional otherwise.
     */
    Optional<Entity> getEntityByType(Type type);

    /**
     * Gets the object that is handling the domain of the game.
     * 
     * @return The World(domain) of the game.
     */
    World getWorld();

    /**
     * Gets current active entities.
     * 
     * @return A list of all current entities.
     */
    List<Entity> getEntities();

    /**
     * Checks if player has won or lost.
     * 
     * @return True if player has won.
     */
    boolean isVictory();
}
