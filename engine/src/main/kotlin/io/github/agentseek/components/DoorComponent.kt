package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.events.NewLevelEvent

/**
 * Component used to describe door's behavior.
 */
class DoorComponent(gameObject: GameObject, val destinationSceneResourceName: String) : AbstractComponent(gameObject) {
    private val rigidBody = gameObject.rigidBody

    override fun init() {
        rigidBody.onCollision { gameObject ->
            if (gameObject.name == PLAYER_GO_NAME) {
                notifyEvent(NewLevelEvent(destinationSceneResourceName))
            }
        }
    }

    companion object {
        const val PLAYER_GO_NAME = "Player"
    }
}
