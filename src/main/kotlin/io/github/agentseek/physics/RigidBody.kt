package io.github.agentseek.physics

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Rectangle2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
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
): AbstractComponent(gameObject) {
    /**
     * A simple rigid body with a circular shape
     */
    class CircleRigidBody(radius: Double, gameObject: GameObject) : RigidBody(Circle2d(radius), gameObject)
    /**
     * A simple rigid body with a rectangular shape
     */
    class RectangleRigidBody(width: Double, height: Double, gameObject: GameObject)
        : RigidBody(Rectangle2d(width, height), gameObject)

    /**
     * Velocity of this [RigidBody], in meters per seconds
     */
    var velocity: Vector2d = Vector2d.zero()

    /**
     * Acceleration of this [RigidBody] in m/s^2
     */
    private var acceleration: Vector2d = Vector2d.zero()

    /**
     * Checks if this hit box is colliding with another [rigidBody], intersecting their two-dimensional shapes.
     */
    fun isCollidingWith(rigidBody: RigidBody): Boolean = shape.intersects(rigidBody.shape)

    override fun onUpdate(deltaTime: Duration) {
        val elapsed = deltaTime.toDouble(DurationUnit.SECONDS)
        velocity += acceleration * elapsed
        shape.position += velocity * elapsed
        acceleration = Vector2d.zero()
    }

    /**
     * Force in newtons. Changes Rigid body [acceleration]
     */
    fun applyForce(force: Vector2d) {
        acceleration += force
    }

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
