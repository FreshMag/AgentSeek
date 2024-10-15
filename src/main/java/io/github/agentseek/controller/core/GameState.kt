package io.github.agentseek.controller.core

import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Type
import io.github.agentseek.world.World

/**
 * This interface represents the state of the game and generates new levels based on a LevelHandler.
 */
interface GameState {
    /**
     * Reference to current player.
     */
    val player: GameObject

    /**
     * Gets the [World] representing the domain of the game.
     */
    val world: World

    /**
     * Gets current active [GameObject]s.
     */
    val entities: Iterable<GameObject>

    /**
     * Checks if player has won or lost.
     */
    val isVictory: Boolean

    /**
     * Checks if game is over and if player has won or lost.
     */
    val isGameOver: Boolean

    /**
     * Add a [GameObject] to the current GameState.
     */
    fun addGameObject(gameObject: GameObject)

    /**
     * Removes a [GameObject] to current GameState.
     */
    fun removeGameObject(gameObject: GameObject)

    /**
     * Generates a new Level and spawns all entities inside it, player too.
     */
    fun generateNewLevel()

    /**
     * Updates current GameState, passing the [deltaTime] of time elapsed since last update.
     */
    fun updateState(deltaTime: Double)

    /**
     * Sets flags that indicates that the game is over: if [victory] is `true`, the game ended with a victory.
     */
    fun callGameOver(victory: Boolean)

    // val view: ViewHandlerImpl?

    // val levelHandler: LevelHandler

    /**
     * Gets the [GameObject]s that have the given [type]
     */
    fun getGameObjectsByType(type: Type): Iterable<GameObject>

}
