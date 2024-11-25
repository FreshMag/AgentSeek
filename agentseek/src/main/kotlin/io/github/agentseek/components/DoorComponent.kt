package io.github.agentseek.components

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.NewLevelEvent

/**
 * Component used to describe door's behavior.
 */
class DoorComponent(
    gameObject: GameObject,
    /**
     * Name of the scene resource to load when the player collides with the door.
     */
    val destinationSceneResourceName: String
) : AbstractComponent(gameObject) {
    private val rigidBody = gameObject.rigidBody

    override fun init() {
        rigidBody.onCollision { gameObject ->
            if (gameObject.name == Config.Player.name) {
                notifyEvent(NewLevelEvent(destinationSceneResourceName))
            }
        }
    }
}