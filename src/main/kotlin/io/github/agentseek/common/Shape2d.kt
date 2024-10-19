package io.github.agentseek.common

/**
 * This interface represents a Shape in two dimensions.
 */
interface Shape2d {
    /**
     * Returns the center of this [Shape2d].
     */
    var center: Point2d
    /**
     * The [Shape2d] position in the 2D space, represented as a [Point2d].
    */
    var position: Point2d

    /**
     * Checks if the [point] is contained within the [Shape2d].
     */
    fun contains(point: Point2d): Boolean

    /**
     * Check if this [Shape2d] intersects with [shape].
     */
    fun intersects(shape: Shape2d): Boolean

}