package io.github.agentseek.components

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.NewLevelEvent

class GameOverComponent(gameObject: GameObject): AbstractComponent(gameObject) {

    class GameOverEvent: NewLevelEvent("GameOver")

    override fun init() {
        gameObject.rigidBody.onCollision { gameObject ->
            if (gameObject.name == Config.Player.name) {
                notifyEvent(GameOverEvent())
            }
        }
    }
}