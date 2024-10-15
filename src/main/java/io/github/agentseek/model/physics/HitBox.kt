package io.github.agentseek.model.physics

import io.github.agentseek.common.Shape2d

/**
 * Hit boxes are zones attached to a game object that represent where that object is collidable.
 */
interface HitBox {
    /**
     * Gets the [form] of this hit box. Hit boxes should have two-dimensional geometric forms, like rectangle or circle.
     */
    val form: Shape2d

    /**
     * Checks if this hit box is colliding with another [hitBox], intersecting their two-dimensional shapes.
     */
    fun isCollidingWith(hitBox: HitBox): Boolean

    /**
     * Checks if this hit box is out of [bounds].
     */
    fun isOutOfBounds(bounds: Shape2d): Boolean
}
