package io.github.agentseek.items

import io.github.agentseek.controller.core.GameEngine
import io.github.agentseek.controller.core.GameState
import io.github.agentseek.components.HealthIntComponent
import io.github.agentseek.core.getComponent

/**
 * This type of item heals player's hps by the maximum hps.
 */
class HealItem : Item {
    /**
     * Total recover of the player's health
     */
    override fun apply(state: GameState) {
        state.player.getComponent<HealthIntComponent>()?.maxOutHp()
        GameEngine.runDebugger { println("heal") }
    }
}
