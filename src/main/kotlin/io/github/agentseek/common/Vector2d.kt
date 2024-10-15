package io.github.agentseek.common

import kotlin.math.sqrt

/**
 * Two-dimensional vector, with a [x] component and [y] component
 */
class Vector2d(var x: Double, var y: Double) {

    /**
     * Instantiates a vector from two points, [origin] and [destination]
     */
    constructor(origin: Point2d, destination: Point2d) : this(origin.x, origin.y) {
        x -= destination.x
        y -= destination.y
    }

    /**
     * Return the difference between this vector and another [vector].
     */
    operator fun minus(vector: Vector2d): Vector2d = Vector2d(x - vector.x, y - vector.y)

    /**
     * Module of this vector.
     */
    fun module(): Double = sqrt(x * x + y * y)

    /**
     * This vector normalized.
     */
    fun normalized(): Vector2d =
        Vector2d(x / module(), y / module())

    /**
     * Multiplies this vector by a [factor].
     */
    operator fun times(factor: Double): Vector2d = Vector2d(x * factor, y * factor)

    /**
     * Sums this vector with [vector]
     */
    operator fun plus(vector: Vector2d): Vector2d = Vector2d(x + vector.x, y + vector.y)

    companion object {
        /**
         * Generate a new vector given the angle in [degrees] with x-axis.
         */
        fun fromAngle(degrees: Double): Vector2d =
            Vector2d(
                GameMath.cosDeg(degrees.toFloat().toDouble()),
                GameMath.sinDeg(degrees.toFloat().toDouble())
            )

        fun zero(): Vector2d = Vector2d(0.0, 0.0)

        fun fromPosition(position: Point2d): Vector2d = Vector2d(position.x, position.y)
    }
}
