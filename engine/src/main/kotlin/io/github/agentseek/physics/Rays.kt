package io.github.agentseek.physics

import io.github.agentseek.common.*
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min

object Rays {

    class Ray(val gameObject: GameObject, vector2d: Vector2d) {
        val direction = vector2d.normalized()

        /**
         * Returns the first GameObject intersecting with this Ray
         */
        val firstIntersecting: GameObject?
            get() = findFirstIntersecting()

        /**
         * Returns all GameObjects intersecting with this Ray
         */
        val allIntersecting: List<GameObject>
            get() = findAllIntersecting()

        private fun findFirstIntersecting(): GameObject? =
            gameObject.otherGameObjects()
                .firstOrNull { it.rigidBody.shape.rayIntersect(this) }

        private fun findAllIntersecting(): List<GameObject> =
            gameObject.otherGameObjects()
                .filter { it.rigidBody.shape.rayIntersect(this) }
                .sortedBy { (gameObject.position - it.position).module() }

    }

    /**
     * Casts a ray from this GameObject with a specified direction (vector 2d)
     */
    fun GameObject.castRay(vector2d: Vector2d): Ray = Ray(this, vector2d)

    /**
     * Casts a ray from this GameObject to another (using the vector between their position)
     */
    fun GameObject.castRay(gameObject: GameObject): Ray =
        Ray(this, this.position - gameObject.position)

    /**
     * Checks if this Shape intersects with a [Ray]
     */
    fun Shape2d.rayIntersect(ray: Ray): Boolean =
        when (this) {
            is Circle2d -> this.circleRayIntersect(ray)
            is Rectangle2d -> this.rectangleRayIntersect(ray)
            is Cone2d -> this.coneRayIntersect(ray)
        }


    private fun Circle2d.circleRayIntersect(ray: Ray): Boolean {
        val oc = ray.gameObject.position - center
        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.dot(direction)
        val b = 2.0 * oc.dot(direction)
        val c = oc.dot(oc) - radius * radius

        val discriminant = b * b - 4 * a * c
        return discriminant >= 0
    }

    private fun Rectangle2d.rectangleRayIntersect(ray: Ray): Boolean {
        val invDir = Vector2d(1.0 / ray.direction.x, 1.0 / ray.direction.y)

        val tMin = (upperLeft - ray.gameObject.position).componentWiseMul(invDir)
        val tMax = (lowerRight - ray.gameObject.position).componentWiseMul(invDir)

        val t1 = min(tMin.x, tMax.x)
        val t2 = max(tMin.x, tMax.x)
        val t3 = min(tMin.y, tMax.y)
        val t4 = max(tMin.y, tMax.y)

        val tNear = max(t1, t3)
        val tFar = min(t2, t4)

        // If tNear > tFar, no intersection
        return tNear <= tFar && tFar >= 0.0
    }

    private fun Cone2d.coneRayIntersect(ray: Ray): Boolean {
        val oc = ray.gameObject.position - vertex
        val cosThetaSq = cos(angle) * cos(angle)

        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.x * direction.x + direction.y * direction.y - cosThetaSq * direction.dot(direction)
        val b = 2.0 * (oc.x * direction.x + oc.y * direction.y - cosThetaSq * oc.dot(direction))
        val c = oc.x * oc.x + oc.y * oc.y - cosThetaSq * oc.dot(oc)

        val discriminant = b * b - 4 * a * c

        return discriminant >= 0
    }
}