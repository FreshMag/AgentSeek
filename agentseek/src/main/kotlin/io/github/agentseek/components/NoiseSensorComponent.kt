package io.github.agentseek.components

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Point2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Collider
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import io.github.agentseek.view.utilities.Rendering.fillGradientCircle
import java.awt.Color
import kotlin.time.Duration

class NoiseSensorComponent(gameObject: GameObject, radius: Double) : AbstractComponent(gameObject),
    Sensor<List<NoiseSensorComponent.Perception>> {
    private val noiseSensorCollider: Collider = Collider.CircleCollider(radius, gameObject)
    private var lastPos = gameObject.position

    private var reactions = listOf<(List<Perception>) -> Unit>()
    var noiseColor: Color = Config.VisualComponents.noiseSensorColor

    data class Perception(val gameObject: GameObject, val noisePosition: Point2d)

    override fun init() {
        noiseSensorCollider.center = gameObject.center()
        gameObject.attachRenderer { _, renderingContext ->
            renderingContext?.fillGradientCircle(
                noiseSensorCollider.shape as Circle2d, noiseColor, Color(255, 255, 0, 0)
            )
        }
    }

    override fun onUpdate(deltaTime: Duration) {
        noiseSensorCollider.center = gameObject.center()
        if (lastPos != gameObject.position) {
            lastPos = gameObject.position
        }
        val perceptions: List<Perception> = gameObject.otherGameObjects().mapNotNull { go ->
            go.getComponent<NoiseEmitterComponent>()?.let { noiseEmitter ->
                noiseEmitter.getNoiseEmitterCollider()?.let {
                    if (it.isCollidingWith(noiseSensorCollider)) {
                        return@mapNotNull Perception(go, it.center)
                    }
                }
            }
            go.getComponent<MouseNoiseEmitterComponent>()?.let { mouseEmitter ->
                mouseEmitter.getNoiseEmitterCollider()?.let {
                    if (it.isCollidingWith(noiseSensorCollider)) {
                        return@mapNotNull Perception(go, it.center)
                    }
                }
            }
            return@mapNotNull null

        }
        reactions.forEach { it(perceptions) }
    }

    override fun addReaction(reaction: (List<Perception>) -> Unit) {
        reactions += reaction
    }
}