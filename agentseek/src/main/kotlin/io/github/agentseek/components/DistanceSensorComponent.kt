package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities.allDirections8

/**
 * Component that calculates the resultant vector of distances from nearby objects within a certain radius
 */
class DistanceSensorComponent(
    gameObject: GameObject, private val radius: Double
) : AbstractComponent(gameObject) {

    /**
     * Gets the resultant obtained adding all vectors of distances from nearby objects within [radius]
     */
    fun getDistancesResultant(): Vector2d = calculateDanger()

    /**
     * Calculates the resultant vector of distances from nearby objects
     */
    private fun Iterable<Vector2d>.sumUp(): Vector2d = fold(Vector2d.zero()) { acc, vector -> acc + vector }

    /**
     * Calculates the danger vector using the eight directions around the agent
     */
    private fun calculateDanger(): Vector2d {
        return (allDirections8())
            .mapNotNull { direction ->
                val first = gameObject.castRay(direction).firstIntersecting
                first?.let {
                    if (it.gameObject.name != Config.Player.name && it.distance < radius) {
                        direction * (radius - it.distance)
                    } else {
                        null
                    }
                }
            }
            .sumUp()
    }

}