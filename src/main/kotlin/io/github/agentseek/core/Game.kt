package io.github.agentseek.core

import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * This interface represents the state of the game and generates new levels based on a LevelHandler.
 */
interface Game {
    /**
     * Reference to current player.
     */
    val player: GameObject?

    /**
     * GameObjects of the world
     */
    val gameObjects: Iterable<GameObject>
        get() = world.gameObjects

    /**
     * Gets the [World] representing the domain of the game.
     */
    val world: World

    /**
     * Checks if player has won or lost.
     */
    val isVictory: Boolean

    /**
     * Checks if game is over and if player has won or lost.
     */
    val isGameOver: Boolean

    /**
     * Updates current GameState, passing the [deltaTime] of time elapsed since last update.
     */
    fun updateState(deltaTime: Duration)

    /**
     * Sets flags that indicates that the game is over: if [victory] is `true`, the game ended with a victory.
     */
    fun callGameOver(victory: Boolean)

    companion object {
        fun emptyScene(): Game = GameImpl()
    }

}
