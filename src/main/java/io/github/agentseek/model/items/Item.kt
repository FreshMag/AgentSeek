package io.github.agentseek.model.items

import io.github.agentseek.controller.core.GameState

/**
 * Interface of an Item. Classes that implement this interface consist in various bonuses that the player can obtain.
 */
interface Item {
    /**
     * Performs some actions depending on the Item type. It requires the [state] to function.
     */
    fun apply(state: GameState)
}
