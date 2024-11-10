package io.github.agentseek.common

import kotlin.math.*

/**
 * Two-dimensional vector, with a [x] component and [y] component
 */
data class Vector2d(var x: Double, var y: Double) {

    /**
     * Instantiates a vector from two points, [origin] and [destination]
     */
    constructor(origin: Point2d, destination: Point2d) : this(origin.x, origin.y) {
        x -= destination.x
        y -= destination.y
    }

    constructor(x: Int, y: Int) : this(x.toDouble(), y.toDouble())

    /**
     * Returns the left-hand perpendicular vector to this one.
     */
    val leftNormal: Vector2d get() = this.rotateDegrees(90.0)

    /**
     * Returns the right-hand perpendicular vector to this one.
     */
    val rightNormal: Vector2d get() = this.rotateDegrees(-90.0)

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
     * Divides a vector by a [factor] (equivalent to v * (1/factor))
     */
    operator fun div(factor: Double): Vector2d = Vector2d(x / factor, y / factor)

    /**
     * Sums this vector with [vector]
     */
    operator fun plus(vector: Vector2d): Vector2d = Vector2d(x + vector.x, y + vector.y)

    /**
     * Inverts this vector.
     */
    operator fun unaryMinus(): Vector2d = Vector2d(-x, -y)

    /**
     * Computes the dot product of two vectors
     */
    fun dot(other: Vector2d): Double {
        return this.x * other.x + this.y * other.y
    }

    /**
     * Returns the angle in radians formed by this vector and [other]
     */
    fun angleWith(other: Vector2d): Double {
        val dotProduct = this.dot(other)
        val magnitudeProduct = this.module() * other.module()
        if (magnitudeProduct == 0.0) return 0.0
        val cosTheta = (dotProduct / magnitudeProduct).coerceIn(-1.0, 1.0)

        return acos(cosTheta)
    }

    fun angle(): Double {
        return atan2(y, x)
    }

    /**
     * Computes the component-wise product of two vectors (i.e. (v1.x * v2.x, v1.y * v2.y)
     */
    fun componentWiseMul(v: Vector2d): Vector2d {
        return Vector2d(this.x * v.x,this.y * v.y)
    }

    /**
     * Gets the perpendicular vector (anti-clock wise) to this one
     */
    fun perpendicular(): Vector2d {
        return Vector2d(-y, x)
    }

    /**
     * Finds the rotated vector by [angle] (in radians)
     */
    fun rotateRadians(angle: Double): Vector2d {
        val cosTheta = cos(angle)
        val sinTheta = sin(angle)

        val newX = x * cosTheta - y * sinTheta
        val newY = x * sinTheta + y * cosTheta

        return Vector2d(newX, newY)
    }

    /**
     * Finds the rotated vector by [angle] (in degrees)
     */
    fun rotateDegrees(degrees: Double): Vector2d {
        val radians = Math.toRadians(degrees)
        return this.rotateRadians(radians)
    }

    companion object {
        fun zero(): Vector2d = Vector2d(0.0, 0.0)

        fun fromPosition(position: Point2d): Vector2d = Vector2d(position.x, position.y)
    }
}
