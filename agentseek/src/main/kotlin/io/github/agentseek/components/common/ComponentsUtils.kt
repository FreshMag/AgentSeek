package io.github.agentseek.components.common

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities
import kotlin.math.sqrt

object ComponentsUtils {

    /**
     * Calculates a random velocity by casting rays in all directions from the given game object
     * and selecting the direction of the furthest intersecting point.
     *
     * @param gameObject The game object from which to cast rays.
     * @return A Vector2d representing the calculated random velocity.
     */
    fun getRandomVelocity(gameObject: GameObject): Vector2d =
        FastEntities.allDirections().sortedBy { gameObject.castRay(it).firstIntersecting?.distance ?: 0.0 }.drop(1)
            .let { directions ->
                val weightedRandomIndex =
                    (0 until directions.size).flatMap { index -> List(index + 1) { index } }.random()
                directions[weightedRandomIndex]
            }

    /**
     * Checks if the distance between two points in a 2D space is within the specified maximum distance.
     *
     * @param firstWorldPoint The first point with x and y coordinates.
     * @param secondWorldPoint The second point with x and y coordinates.
     * @param maxDistance The maximum distance to check between the two points.
     * @return `true` if the distance between the points is within the maxDistance, `false` otherwise.
     */
    fun isPointWithinDistance(firstWorldPoint: Point2d, secondWorldPoint: Point2d, maxDistance: Double): Boolean {
        val dx = firstWorldPoint.x - secondWorldPoint.x
        val dy = firstWorldPoint.y - secondWorldPoint.y
        return sqrt(dx * dx + dy * dy) <= maxDistance
    }
}