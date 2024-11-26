package io.github.agentseek.components

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.NewLevelEvent

/**
 * Component that triggers a GameOverEvent when the player collides with an object
 */
class GameOverComponent(gameObject: GameObject): AbstractComponent(gameObject) {

    /**
     * Event that triggers when the player collides with an object
     */
    private class GameOverEvent: NewLevelEvent("GameOver")

    override fun init() {
        gameObject.rigidBody.onCollision { gameObject ->
            if (gameObject.name == Config.Player.name) {
                notifyEvent(GameOverEvent())
            }
        }
    }
}