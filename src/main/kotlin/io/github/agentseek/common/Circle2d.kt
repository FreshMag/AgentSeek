package io.github.agentseek.common

/**
 * This class represents a Circle in two dimensions, with [position] equal to its top left corner
 * and [radius]
 */
data class Circle2d(val radius: Int, override var position: Point2d = Point2d(0.0, 0.0)) : Shape2d {
    override var center: Point2d
        get() = position
        set(value) {
            position = Point2d(value.x - (radius / 2.0), value.y - (radius / 2.0))
        }

    override fun contains(point: Point2d): Boolean = Vector2d(center, point).module() <= radius

    override fun intersect(shape: Shape2d): Boolean =
        when(shape) {
            is Circle2d -> (Vector2d(center, shape.center).module() <= radius + shape.radius)
            is Rectangle2d -> TODO()
            else -> false
        }

    override fun toString(): String = "Circle2d [radius=$radius, center=$center]"

}
