package io.github.agentseek.controller.core

import io.github.agentseek.controller.Entity
import io.github.agentseek.model.Type
import io.github.agentseek.model.World
import java.util.*

/**
 * This interface represents the state of the game and generates new levels based on a LevelHandler.
 */
interface GameState {
    /**
     * @return a reference to current player.
     */
    val player: Entity

    /**
     * Add an Entity to the current GameState.
     *
     * @param entity Entity to be added.
     */
    fun addEntity(entity: Entity)

    /**
     * Generates a new Level and spawns all entities inside it, player too.
     */
    fun generateNewLevel()

    /**
     * Removes an Entity to current GameState.
     *
     * @param entity Entity to be removed.
     */
    fun removeEntity(entity: Entity)

    /**
     * Updates current GameState of a certain amount of time elapsed.
     *
     * @param dt Delta time, time elapsed after last update.
     */
    fun updateState(dt: Double)

    /**
     * Checks if game is over and if player has won or lost.
     *
     * @return True if the game is over.
     */
    val isGameOver: Boolean

    /**
     * Sets flags that indicates that the game is over.
     *
     * @param victory True if player has won, false if player has lost.
     */
    fun callGameOver(victory: Boolean)

    /**
     * Gets the object that is handling the view.
     *
     * @return The handler of the view.
     */
    val view: ViewHandlerImpl?

    /**
     * Gets the object that is handling the Levels System.
     *
     * @return The handler of the levels system.
     */
    val levelHandler: LevelHandler?

    /**
     * Gets an Entity whose type corresponds to the type given in input, taken from
     * the current active entities.
     *
     * @param type Type of the Entity.
     * @return An Optional of Entity if an Entity with the determined type was
     * present, or an empty Optional otherwise.
     */
    fun getEntityByType(type: Type): Optional<Entity?>?

    /**
     * Gets the object that is handling the domain of the game.
     *
     * @return The World(domain) of the game.
     */
    val world: World

    /**
     * Gets current active entities.
     *
     * @return A list of all current entities.
     */
    val entities: List<Any?>?

    /**
     * Checks if player has won or lost.
     *
     * @return True if player has won.
     */
    val isVictory: Boolean
}
