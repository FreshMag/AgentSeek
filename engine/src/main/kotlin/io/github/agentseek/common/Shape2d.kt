package io.github.agentseek.common

/**
 * This interface represents a Shape in two dimensions.
 */
sealed class Shape2d {
    /**
     * Returns the center of the enclosing rectangle of this [Shape2d].
     */
    abstract var center: Point2d
    /**
     * The upper left corner position of the enclosing rectangle, represented as a [Point2d].
    */
    abstract var position: Point2d

    /**
     * Width of the enclosing rectangle
     */
    abstract val width: Double

    /**
     * Height of the enclosing rectangle
     */
    abstract val height: Double

    /**
     * Checks if the [point] is contained within the [Shape2d].
     */
    abstract fun contains(point: Point2d): Boolean

    /**
     * Check if this [Shape2d] intersects with [shape].
     */
    abstract fun intersects(shape: Shape2d): Boolean

}