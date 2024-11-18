package io.github.agentseek.components.common

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.util.FastEntities

object ComponentsUtils {

    fun getRandomVelocity(gameObject: GameObject): Point2d =
        FastEntities.allDirections().mapNotNull { gameObject.castRay(it).firstIntersecting }
            .maxBy { it.distance }.gameObject.position
}