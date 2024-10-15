package io.github.agentseek.items

import io.github.agentseek.controller.core.GameEngine
import io.github.agentseek.controller.core.GameState
import io.github.agentseek.components.HealthIntComponent
import io.github.agentseek.core.getComponent

/**
 * This type of Item augments player's max hp by one.
 */
class MaxHealthItem : Item {
    /**
     * Increases the max health of the player
     */
    override fun apply(state: GameState) {
        state.player.getComponent<HealthIntComponent>()?.increaseMaxHp(1)
        GameEngine.runDebugger { println("max heal") }
    }
}
