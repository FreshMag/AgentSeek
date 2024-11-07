package io.github.agentseek.physics

import io.github.agentseek.common.*
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects

sealed class Collider(val shape: Shape2d, val gameObject: GameObject) {

    var position: Point2d = shape.position
        get() = shape.position
        set(value) {
            shape.position = value
            field = value
        }

    var center: Point2d = shape.center
        get() = shape.center
        set(value) {
            shape.center = value
            field = value
        }
    /**
     * Traverses the rigid body of the parent game object. Note that this Collider might not be associated with that
     * rigid body
     */
    val rigidBody: RigidBody
        get() = gameObject.rigidBody

    /**
     * A simple collider with a circular shape
     */
    class CircleCollider(radius: Double, gameObject: GameObject) : Collider(Circle2d(radius), gameObject)

    /**
     * A simple collider with a rectangular shape
     */
    class RectangleCollider(width: Double, height: Double, gameObject: GameObject) :
        Collider(Rectangle2d(width, height), gameObject)

    /**
     * A simple collider with a cone shape
     */
    class ConeCollider(angle: Double, length: Double, rotation: Double, gameObject: GameObject) :
        Collider(Cone2d(Point2d.origin(), angle, length, rotation), gameObject)

    /**
     * Collider that does nothing.
     */
    class EmptyCollider(gameObject: GameObject) : Collider(Circle2d(0.0), gameObject) {
        override fun isCollidingWith(collider: Collider): Boolean = false
    }

    /**
     * Checks if this collider is colliding with another [collider], by intersecting their two-dimensional shapes.
     */
    open fun isCollidingWith(collider: Collider): Boolean {
        return shape.intersects(collider.shape)
    }

    /**
     * Finds the rigid bodies' colliders colliding with this collider
     */
    fun findColliding(): List<Collider> =
        gameObject.otherGameObjects()
            .map { it.rigidBody.collider }
            .filter { isCollidingWith(it) }
            .toList()
}