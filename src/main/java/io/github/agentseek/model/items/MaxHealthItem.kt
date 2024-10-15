package io.github.agentseek.model.items

import io.github.agentseek.controller.core.GameEngine
import io.github.agentseek.controller.core.GameState
import io.github.agentseek.model.components.HealthIntComponent
import io.github.agentseek.controller.getComponent

/**
 * This type of Item augments player's max hp by one.
 */
class MaxHealthItem : Item {
    /**
     * Increases the max health of the player
     */
    override fun apply(state: GameState) {
        state.player.gameObject.getComponent<HealthIntComponent>()?.increaseMaxHp(1)
        GameEngine.runDebugger { println("max heal") }
    }
}
