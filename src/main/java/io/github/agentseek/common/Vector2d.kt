package io.github.agentseek.common

import kotlin.math.sqrt

/**
 * Two dimensional vector.
 * @param x Component.
 * @param y Component.
 * @constructor empty
 */
class Vector2d(var x: Double, var y: Double) {

    /**
     * Instantiates a vector from two points.
     *
     * @param origin Point of application of the vector.
     * @param destination Second point.
     */
    constructor(origin: Point2d, destination: Point2d) : this(origin.x, origin.y) {
        x -= destination.x
        y -= destination.y
    }

    /**
     * Return the difference between two vectors.
     *
     * @param vector Vector to be subtracted.
     * @return Difference between the two vectors.
     */
    fun sub(vector: Vector2d): Vector2d = Vector2d(x - vector.x, y - vector.y)


    /**
     * Module of the vector.
     *
     * @return The module of the vector.
     */
    fun module(): Double = sqrt(x * x + y * y)


    /**
     * Vector normalized.
     *
     * @return The vector normalized.
     */
    fun normalized(): Vector2d {
        val module = sqrt(x * x + y * y)
        return Vector2d(x / module, y / module)
    }

    /**
     * Multiplies this vector by a factor, without changing his state.
     *
     * @param factor Factor.
     * @return Result of multiplication as a new two-dimensional vector.
     */
    fun multiply(factor: Double): Vector2d = Vector2d(x * factor, y * factor)


    /**
     * Multiplies this vector by a factor, changing his state.
     *
     * @param factor Factor.
     * @return Result of multiplication as a new two-dimensional vector.
     */
    fun mulLocal(factor: Double): Vector2d {
        x *= factor
        y *= factor
        return Vector2d(x, y)
    }

    /**
     * Sum of two vectors. ATTENTION: this vector's state will be changed!
     *
     * @param vector Vector sum.
     * @return The sum of the two vectors.
     */
    fun addLocal(vector: Vector2d): Vector2d {
        x += vector.x
        y += vector.y
        return Vector2d(x, y)
    }

    /**
     * Generate a new vector given the angle with x-axis.
     *
     * @param degrees Angle.
     * @return Vector generated.
     */
    fun fromAngle(degrees: Double): Vector2d {
        return Vector2d(
            GameMath.cosDeg(degrees.toFloat().toDouble()),
            GameMath.sinDeg(degrees.toFloat().toDouble())
        )
    }
}
