package ryleh.common;
/**
 * This interface represents a Shape in two dimensions.
 */
public interface Shape2d {
    /**
     * Checks if the point is contained within the Shape2d.
     * 
     * @param point Point2d value.
     * @return boolean value.
     */
    boolean contains(Point2d point);

    /**
     * Sets the Shape2d position to new value.
     * 
     * @param position Point2d value.
     */
    void setPosition(Point2d position);

    /**
     * Gets the Shape2d position.
     * 
     * @return Point2d value.
     */
    Point2d getPosition();

    /**
     * Check if two Shape2d instances intersect.
     * 
     * @param shape Shape2d value.
     * @return boolean value.
     */
    boolean intersect(Shape2d shape);

    /**
     * Return the center of Shape2d.
     * 
     * @return Point2d value.
     */
    Point2d getCenter();
}
