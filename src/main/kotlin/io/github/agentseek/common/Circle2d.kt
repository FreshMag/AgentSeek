package io.github.agentseek.common

/**
 * This class represents a Circle in two dimensions.
 * @param center Center of the circle.
 * @param radius Radius of the circle, by default is (0.0, 0.0).
 */
class Circle2d(val radius: Int, var center: Point2d) : Shape2d {

    /**
     * {@inheritDoc}
     */
    override fun contains(point: Point2d): Boolean = Vector2d(center, point).module() <= radius

    /**
     * {@inheritDoc}
     */
    override fun setPosition(position: Point2d) {
        center = position
    }

    /**
     * {@inheritDoc}
     */
    override fun getPosition(): Point2d = center

    /**
     * {@inheritDoc}
     */
    override fun intersect(shape: Shape2d): Boolean {
        if (shape is Circle2d) {
            return (Vector2d(center, shape.center).module() <= radius
                    + shape.radius)
        }
        return false
    }

    /**
     * {@inheritDoc}
     */
    override fun getCenter(): Point2d = center

    /**
     * {@inheritDoc}
     */
    override fun toString(): String = "Circle2d [radius=$radius, center=$center]"

}
