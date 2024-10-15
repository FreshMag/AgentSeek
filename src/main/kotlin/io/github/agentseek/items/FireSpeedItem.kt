package io.github.agentseek.items

import io.github.agentseek.controller.core.GameEngine
import io.github.agentseek.controller.core.GameState
import io.github.agentseek.components.ShootingComponent
import io.github.agentseek.core.getComponent

/**
 * This type of item augments player's attack speed by a factor.
 */
class FireSpeedItem : Item {
    /**
     * Increases the attack speed by a default factor.
     */
    override fun apply(state: GameState) {
        state.player.getComponent<ShootingComponent>()?.increaseAtkSpeed(FACTOR)
        GameEngine.runDebugger { println("shoot") }
    }

    companion object {
        private const val FACTOR = 0.35
    }
}
