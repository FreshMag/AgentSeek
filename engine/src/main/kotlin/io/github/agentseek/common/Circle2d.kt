package io.github.agentseek.common

/**
 * This class represents a Circle in two dimensions, with [position] equal to its top left corner
 * and [radius]
 */
data class Circle2d(val radius: Double, override var position: Point2d = Point2d(0.0, 0.0)) : Shape2d() {
    override var center: Point2d = position + Vector2d(radius, radius)
        get() = position + Vector2d(radius, radius)
        set(value) {
            position = Point2d(value.x - radius, value.y - radius)
            field = value
        }
    override val width: Double
        get() = radius * 2
    override val height: Double
        get() = radius * 2

    override fun contains(point: Point2d): Boolean = Vector2d(center, point).module() <= radius

    override fun intersects(shape: Shape2d): Boolean =
        when (shape) {
            is Circle2d -> intersectWithCircle(shape)
            is Rectangle2d -> shape.intersects(this)
            is Cone2d -> false
        }


    private fun intersectWithCircle(circle: Circle2d): Boolean {
        val distanceX = circle.center.x - center.x
        val distanceY = circle.center.y - center.y
        val distance = distanceX * distanceX + distanceY * distanceY
        val radiusSum = radius + circle.radius

        return distance < radiusSum * radiusSum
    }

    override fun toString(): String = "Circle2d [radius=$radius, center=$center]"

}
