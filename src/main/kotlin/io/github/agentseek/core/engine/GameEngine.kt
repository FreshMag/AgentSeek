package io.github.agentseek.core.engine

import io.github.agentseek.core.GameState


/**
 * This is the main class that initiates the game and the main game loop.
 */
class GameEngine(private var gameState: GameState? = null) {

    /**
     * Initializes engine's game state and the frequency (period) of game loop. Key
     * code "P" is set to pause engine.
     */
    fun initGame() {
    }

    /**
     * Initialize the game loop
     */
    fun mainLoop() {
        TODO("rifare game loop")
    }
}
