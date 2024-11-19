package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities.allDirections8
import io.github.agentseek.util.GameObjectUtilities.attachRenderer
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.view.utilities.Rendering.drawVector
import java.awt.Color

class DistanceSensorComponent(
    gameObject: GameObject, private val radius: Double
) : AbstractComponent(gameObject) {

    var debugVectors: List<Vector2d> = emptyList()

    override fun init() {
        gameObject.attachRenderer { _, context ->
            debugVectors.forEach { v -> context?.drawVector(gameObject.center(), v, Color.RED) }
        }
    }

    /**
     * Gets the resultant obtained adding all vectors of distances from nearby objects within [radius]
     */
    fun getDistancesResultant(): Vector2d = calculateDanger()

    private fun Iterable<Vector2d>.sumUp(): Vector2d = fold(Vector2d.zero()) { acc, vector -> acc + vector }

    private fun calculateDanger(): Vector2d {
        return (allDirections8())
            .mapNotNull { direction ->
                val first = gameObject.castRay(direction).firstIntersecting
                first?.let {
                    if (it.distance < radius) {
                        direction * (radius - it.distance)
                    } else {
                        null
                    }
                }
            }
            .also {
                debugVectors = it
            }
            .sumUp()
    }

}