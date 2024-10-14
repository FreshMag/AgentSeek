package io.github.agentseek.common

/**
 * A two-dimensional point.
 * @param x coordinate.
 * @param y coordinate.
 * @constructor Creates an empty group.
 */
class Point2d(
    var x: Double,
    var y: Double
) {

    /**
     * Sums to this point a vector.
     *
     * @param v Vector to be summed to the point.
     * @return A new P2d representing the sum between this point and vector.
     */
    fun sum(v: Vector2d): Point2d = Point2d(x + v.x, y + v.y)


    /**
     * Vector from this point to another.
     *
     * @param point Second point
     * @return Vector from this point to the point given in input.
     */
    fun sub(point: Point2d): Vector2d = Vector2d(x - point.x, y - point.y)

}
