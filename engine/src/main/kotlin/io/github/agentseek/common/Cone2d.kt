package io.github.agentseek.common

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * This class represents a cone in two dimensions.
 */
data class Cone2d(
    var vertex: Point2d,
    /**
     * Cone aperture (cone angle in radians)
     */
    var angle: Double,
    /**
     * Cone extension
     */
    var length: Double,
    var rotation: Double
) : Shape2d {

    override fun contains(point: Point2d): Boolean {
        val toPoint = (point - vertex).normalized()
        val angleToPoint = acos(toPoint.dot(Vector2d(cos(rotation), sin(rotation))))
        if (angleToPoint > angle / 2) {
            return false
        }

        val distanceToPoint = (point - vertex).module()
        return distanceToPoint <= length
    }

    override var center: Point2d = vertex
        get() = vertex
        set(value) {
            vertex = value
            field = value
        }

    override var position: Point2d = vertex
        get() = vertex
        set(value) {
            vertex = value
            field = value
        }

    override fun intersects(shape: Shape2d): Boolean {
        return when (shape) {
            is Circle2d -> intersectWithCircle(shape)
            is Rectangle2d -> intersectWithRectangle(shape)
            is Cone2d -> false
            else -> false
        }
    }

    private fun intersectWithCircle(circle: Circle2d): Boolean {
        val toCircle = (circle.center - vertex).normalized()
        val angleToCircle = acos(toCircle.dot(Vector2d(cos(rotation), sin(rotation))))

        if (angleToCircle > angle / 2) {
            return false
        }

        val distanceToCircle = (circle.center - vertex).module()
        return distanceToCircle <= length + circle.radius
    }

    private fun intersectWithRectangle(rect: Rectangle2d): Boolean =
        rect.vertices().any { contains(it) }
}
