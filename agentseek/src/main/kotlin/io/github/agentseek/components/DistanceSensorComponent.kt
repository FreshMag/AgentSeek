package io.github.agentseek.components

import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities.allDirections8

class DistanceSensorComponent(
    gameObject: GameObject, private val radius: Double
) : AbstractComponent(gameObject) {

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
                    if (it.gameObject.name != PLAYER_NAME && it.distance < radius) {
                        direction * (radius - it.distance)
                    } else {
                        null
                    }
                }
            }
            .sumUp()
    }

    companion object {
        const val PLAYER_NAME = "Player"
    }

}