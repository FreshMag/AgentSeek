package io.github.agentseek.physics

import io.github.agentseek.common.*
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import kotlin.math.*

object Rays {

    class Ray(val gameObject: GameObject, vector2d: Vector2d) {
        val direction = vector2d.normalized()
        private var other: GameObject? = null

        constructor(origin: GameObject, destination: GameObject)
                : this(origin, destination.center() - origin.center()) {
            other = destination
        }

        /**
         * Returns the first GameObject intersecting with this Ray
         */
        val firstIntersecting: Intersection?
            get() = findFirstIntersecting()

        /**
         * Returns all GameObjects intersecting with this Ray
         */
        val allIntersecting: List<Intersection>
            get() = findAllIntersecting()

        private fun findFirstIntersecting(): Intersection? =
            findAllIntersecting().firstOrNull()

        private fun findAllIntersecting(): List<Intersection> =
            gameObject.otherGameObjects()
                .mapNotNull {
                    it.rigidBody.shape.rayIntersection(this)?.let { distance -> Intersection(it, distance) }
                }.sortedBy { it.distance }

    }

    data class Intersection(val gameObject: GameObject, val distance: Double)

    /**
     * Casts a ray from this GameObject with a specified direction (vector 2d)
     */
    fun GameObject.castRay(vector2d: Vector2d): Ray = Ray(this, vector2d)

    /**
     * Casts a ray from this GameObject to another (using the vector between their position)
     */
    fun GameObject.castRay(gameObject: GameObject): Ray =
        Ray(this, gameObject)

    /**
     * Checks if this Shape intersects with a [Ray]
     */
    fun Shape2d.rayIntersection(ray: Ray): Double? =
        when (this) {
            is Circle2d -> this.circleRayIntersect(ray)
            is Rectangle2d -> this.rectangleRayIntersect(ray)
            is Cone2d -> this.coneRayIntersect(ray)
        }


    private fun Circle2d.circleRayIntersect(ray: Ray): Double? {
        val oc = ray.gameObject.center() - center
        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.dot(direction)
        val b = 2.0 * oc.dot(direction)
        val c = oc.dot(oc) - radius * radius

        val discriminant = b * b - 4 * a * c
        // Since this method treats the ray as an infinite straight line, we need to exclude "backwards" shapes
        if (discriminant < 0 || abs(ray.direction.angleWith(oc)) > Math.PI / 2) return null

        val t1 = (-b - sqrt(discriminant)) / (2.0 * a)
        val t2 = (-b + sqrt(discriminant)) / (2.0 * a)

        val tNear = min(t1, t2)

        if (tNear < 0) return null

        return tNear * direction.module()
    }

    private fun Rectangle2d.rectangleRayIntersect(ray: Ray): Double? {
        val invDir = Vector2d(1.0 / ray.direction.x, 1.0 / ray.direction.y)

        val tMin = (upperLeft - ray.gameObject.center()).componentWiseMul(invDir)
        val tMax = (lowerRight - ray.gameObject.center()).componentWiseMul(invDir)

        val t1 = min(tMin.x, tMax.x)
        val t2 = max(tMin.x, tMax.x)
        val t3 = min(tMin.y, tMax.y)
        val t4 = max(tMin.y, tMax.y)

        val tNear = max(t1, t3)
        val tFar = min(t2, t4)

        if (tNear > tFar || tFar < 0.0) return null

        return tNear * ray.direction.module()
    }

    private fun Cone2d.coneRayIntersect(ray: Ray): Double? {
        val oc = ray.gameObject.center() - vertex
        val cosThetaSq = cos(angle) * cos(angle)

        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.x * direction.x + direction.y * direction.y - cosThetaSq * direction.dot(direction)
        val b = 2.0 * (oc.x * direction.x + oc.y * direction.y - cosThetaSq * oc.dot(direction))
        val c = oc.x * oc.x + oc.y * oc.y - cosThetaSq * oc.dot(oc)

        val discriminant = b * b - 4 * a * c
        // Since this method treats the ray as an infinite straight line, we need to exclude "backwards" shapes
        if (discriminant < 0 || abs(ray.direction.angleWith(oc)) > Math.PI / 2) return null
        val t1 = (-b - sqrt(discriminant)) / (2.0 * a)
        val t2 = (-b + sqrt(discriminant)) / (2.0 * a)

        val tNear = min(t1, t2)

        if (tNear < 0) return null
        return tNear * direction.module()
    }
}