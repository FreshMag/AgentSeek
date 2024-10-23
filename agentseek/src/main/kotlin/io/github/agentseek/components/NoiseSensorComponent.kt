package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import kotlin.time.Duration

class NoiseSensorComponent(gameObject: GameObject) : AbstractComponent(gameObject) {
    override fun onUpdate(deltaTime: Duration) {
        super.onUpdate(deltaTime)
        gameObject.world.gameObjects.forEach { gameObject ->
            gameObject.getComponent<NoiseComponent>()?.let { noiseComponent ->
                noiseComponent.getNoiseRigidBody()?.let {
                    if (this.gameObject.rigidBody.isCollidingWith(it)) println("trovato rumorista")
                }
            }
        }
    }
}