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
        Ray(this, gameObject)

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
        val oc = ray.gameObject.center() - center
        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.dot(direction)
        val b = 2.0 * oc.dot(direction)
        val c = oc.dot(oc) - radius * radius

        val discriminant = b * b - 4 * a * c
        // Since this method treats the ray as an infinite straight line, we need to exclude "backwards" shapes
        return discriminant >= 0 && abs(ray.direction.angleWith(oc)) >= Math.PI / 2
    }

    private fun Rectangle2d.rectangleRayIntersect(ray: Ray): Boolean {
        val rayOrigin = ray.gameObject.center()
        val direction = ray.direction

        val minX = upperLeft.x
        val maxX = lowerRight.x
        val minY = upperLeft.y
        val maxY = lowerRight.y
        val t1 = (minX - rayOrigin.x) / direction.x
        val t2 = (maxX - rayOrigin.x) / direction.x
        val t3 = (minY - rayOrigin.y) / direction.y
        val t4 = (maxY - rayOrigin.y) / direction.y
        val tMin = maxOf(min(t1, t2), min(t3, t4))
        val tMax = minOf(max(t1, t2), max(t3, t4))

        return tMin <= tMax && tMax >= 0.0
    }

    private fun Cone2d.coneRayIntersect(ray: Ray): Boolean {
        val oc = ray.gameObject.center() - vertex
        val cosThetaSq = cos(angle) * cos(angle)

        val direction = ray.direction

        // Coefficients for the quadratic equation
        val a = direction.x * direction.x + direction.y * direction.y - cosThetaSq * direction.dot(direction)
        val b = 2.0 * (oc.x * direction.x + oc.y * direction.y - cosThetaSq * oc.dot(direction))
        val c = oc.x * oc.x + oc.y * oc.y - cosThetaSq * oc.dot(oc)

        val discriminant = b * b - 4 * a * c
        // Since this method treats the ray as an infinite straight line, we need to exclude "backwards" shapes
        return discriminant >= 0 && abs(ray.direction.angleWith(oc)) >= Math.PI / 2
    }
}