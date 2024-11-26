package io.github.agentseek.common

/**
 * A two-dimensional point, with [x] coordinate and [y] coordinate
 */
data class Point2d(
    var x: Double,
    var y: Double
) {
    /**
     * Equals to doing `Point2d(0.0, 0.0)`.
     */
    constructor(): this(0.0, 0.0)

    constructor(x: Int, y: Int): this(x.toDouble(), y.toDouble())

    /**
     * Sums to this point a [vector](v)
     * Returns a new [Point2d] representing the sum between this point and vector.
     */
    operator fun plus(v: Vector2d): Point2d = Point2d(x + v.x, y + v.y)

    /**
     * Subtracts from this point a vector [v].
     */
    operator fun minus(v: Vector2d): Point2d = Point2d(x - v.x, y - v.y)

    /**
     * [Vector2d] from this point to another [point].
     */
    operator fun minus(point: Point2d): Vector2d = Vector2d(x - point.x, y - point.y)

    companion object {
        /**
         * Origin point (0, 0).
         */
        fun origin(): Point2d = Point2d(0.0, 0.0)
    }
}
