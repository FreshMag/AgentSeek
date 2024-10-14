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
     * Sets the Shape2d position to new value.
     *
     * @param position Point2d value.
     */
    fun setPosition(position: Point2d)

    /**
     * Gets the Shape2d position.
     *
     * @return Point2d value.
     */
    fun getPosition(): Point2d

    /**
     * Check if two Shape2d instances intersect.
     *
     * @param shape Shape2d value.
     * @return boolean value.
     */
    fun intersect(shape: Shape2d): Boolean

    /**
     * Return the center of Shape2d.
     *
     * @return Point2d value.
     */
    fun getCenter(): Point2d
}