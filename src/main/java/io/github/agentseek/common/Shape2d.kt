package io.github.agentseek.common

/**
 * This interface represents a Shape in two dimensions.
 */
interface Shape2d {
    /**
     * Checks if the point is contained within the Shape2d.
     *
     * @param point Point2d value.
     * @return boolean value.
     */
    fun contains(point: Point2d): Boolean


    /**
     * Check if two Shape2d instances intersect.
     *
     * @param shape Shape2d value.
     * @return boolean value.
     */
    fun intersect(shape: Shape2d): Boolean
}
