package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import io.github.agentseek.view.utilities.Rendering.fillGradientCircle
import io.github.agentseek.view.utilities.Rendering.strokeCircle
import java.awt.Color
import kotlin.time.Duration

class NoiseSensorComponent(gameObject: GameObject, radius: Double) : AbstractComponent(gameObject) {
    private val noiseSensorCollider: Collider = Collider.CircleCollider(radius, gameObject)
    private var lastPos = gameObject.position

    override fun init() {
        noiseSensorCollider.shape.center = gameObject.center()
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.fillGradientCircle(
                noiseSensorCollider.shape as Circle2d, Color(137, 245, 230), Color(255, 255, 0, 0)
            )
            renderingContext?.strokeCircle(
                noiseSensorCollider.shape as Circle2d,
                color = Color.BLACK
            )
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseSensorCollider.shape.center = gameObject.center()
        if (lastPos != gameObject.center()) lastPos = gameObject.center()
        gameObject.otherGameObjects().forEach { go ->
            go.getComponent<NoiseEmitterComponent>()?.let { noiseComponent ->
                noiseComponent.getNoiseEmitterCollider()?.let {
                    if (it.isCollidingWith(noiseSensorCollider)) {
                        println("trovato rumorista")
                    }
                }
            }
        }
    }
}