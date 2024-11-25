package io.github.agentseek.components.common

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.Component
import io.github.agentseek.components.FieldMovementComponent
import io.github.agentseek.components.jason.JasonAgent
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities
import kotlin.math.sqrt

object ComponentsUtils {

    /**
     * Calculates a random objective by casting rays in all directions from the given game object
     * and selecting the direction of the furthest intersecting point.
     *
     * @param gameObject The game object from which to cast rays.
     * @return A [Point2d] representing the objective
     */
    private fun getRandomObjective(gameObject: GameObject): Point2d =
        FastEntities.allDirections().mapNotNull { gameObject.castRay(it).firstIntersecting }
            .maxBy { it.distance }.gameObject.position


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

    /**
     * Sets a random objective for a field movement used by a [JasonAgent].
     */
    fun Component.setRandomObjective(
        randomMovementTimer: TimerImpl,
        maxWanderingSpeed: Double,
        fieldMovementComponent: FieldMovementComponent
    ) {
        if (!randomMovementTimer.isStarted || randomMovementTimer.isElapsed()) {
            randomMovementTimer.restart()
            val randomObjective: Point2d = getRandomObjective(gameObject)
            synchronized(gameObject) {
                fieldMovementComponent.maxVelocity = maxWanderingSpeed
                fieldMovementComponent.wakeUp()
                fieldMovementComponent.objective = randomObjective
            }
        }
    }
}