package io.github.agentseek.physics

import io.github.agentseek.common.*
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * Rigid body for a [GameObject].
 */
sealed class RigidBody(
    /**
     * Gets the [shape] of the collider of this Rigid body.
     */
    val shape: Shape2d,
    /**
     * [GameObject] with this Rigid body
     */
    gameObject: GameObject
) : AbstractComponent(gameObject) {
    /**
     * A simple rigid body with a circular shape
     */
    class CircleRigidBody(radius: Double, gameObject: GameObject) : RigidBody(Circle2d(radius), gameObject)

    /**
     * A simple rigid body with a rectangular shape
     */
    class RectangleRigidBody(width: Double, height: Double, gameObject: GameObject) :
        RigidBody(Rectangle2d(width, height), gameObject)

    /**
     * A simple rigid body with a cone shape
     */
    class ConeRigidBody(angle: Double, length: Double, rotation: Double, gameObject: GameObject) :
        RigidBody(Cone2d(Point2d.origin(), angle, length, rotation), gameObject)

    /**
     * Velocity of this [RigidBody], in meters per seconds
     */
    var velocity: Vector2d = Vector2d.zero()

    /**
     * Mass in kilograms. Default is 1 kg.
     */
    var mass: Double = 1.0


    /**
     * Whether this [RigidBody] can move or not. If `true`, the [GameObject] cannot move.
     */
    var isStatic: Boolean = false

    /**
     * Is `true`, this [RigidBody] bounces of with negative velocity when it hits a static rigid body.
     */
    var doesBounce: Boolean = true


    /**
     * Acceleration of this [RigidBody] in m/s^2
     */
    private var acceleration: Vector2d = Vector2d.zero()
    private var collisionResolved = false
    private var collisionCallbacks = emptyList<(GameObject) -> Unit>()

    /**
     * Checks if this hit box is colliding with another [rigidBody], intersecting their two-dimensional shapes.
     */
    fun isCollidingWith(rigidBody: RigidBody): Boolean {
        return shape.intersects(rigidBody.shape)
    }

    override fun onUpdate(deltaTime: Duration) {
        if (!isStatic) {
            if (!collisionResolved) {
                findColliding().forEach { resolveCollision(it) }
            }
            val elapsed = deltaTime.toDouble(DurationUnit.SECONDS)
            velocity += acceleration * elapsed
            shape.position += velocity * elapsed
        }
        acceleration = Vector2d.zero()
        collisionResolved = false
    }

    private fun resolveCollision(rigidBody: RigidBody) {
        if (rigidBody.isStatic) {
            this.velocity = if (doesBounce) -velocity else Vector2d.zero()
        } else {
            val m1 = this.mass
            val m2 = rigidBody.mass

            val vi1 = this.velocity
            val vi2 = rigidBody.velocity

            val vf1 = (vi1 * (m1 - m2) + vi2 * 2.0 * m2) / (m1 + m2)
            val vf2 = (vi2 * (m2 - m1) + vi1 * 2.0 * m1) / (m1 + m2)

            this.velocity = vf1
            rigidBody.velocity = vf2
        }
        rigidBody.collisionResolved = true
        callCollision(rigidBody.gameObject)
        rigidBody.callCollision(this.gameObject)
    }

    private fun callCollision(gameObject: GameObject) {
        collisionCallbacks.forEach { it(gameObject) }
    }

    /**
     * Adds a collision callback for this [RigidBody]
     */
    fun onCollision(callback: (GameObject) -> Unit) {
        collisionCallbacks += callback
    }

    /**
     * Force in newtons. Changes rigid body [acceleration] basing on its [mass].
     */
    fun applyForce(force: Vector2d) {
        acceleration += force * (1 / mass)
    }

    /**
     * Finds the rigid bodies colliding with this rigid body
     */
    fun findColliding(): List<RigidBody> =
        gameObject.otherGameObjects()
            .map { it.rigidBody }
            .filter { isCollidingWith(it) }
            .onEach { it.collisionResolved = true }
            .toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RigidBody) return false

        if (shape != other.shape) return false
        if (velocity != other.velocity) return false
        if (acceleration != other.acceleration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = shape.hashCode()
        result = 31 * result + velocity.hashCode()
        result = 31 * result + acceleration.hashCode()
        return result
    }

    override fun toString(): String {
        return "RigidBody(shape=$shape, velocity=$velocity)"
    }

}
