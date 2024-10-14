package ryleh.common;

/**
 * This class represents a Circle in two dimensions.
 *
 */
public class Circle2d implements Shape2d {

    private final int radius;
    private Point2d center;

    /**
     * Instantiate a circle given the center and the radius.
     * 
     * @param center Center of the circle.
     * @param radius Radius of the circle.
     */
    public Circle2d(final Point2d center, final int radius) {
        this.radius = radius;
        this.center = center;
    }

    /**
     * Instantiate a circle given the radius. Center will be set to (0,0).
     * 
     * @param radius Radius of the circle.
     */
    public Circle2d(final int radius) {
        this.radius = radius;
        this.center = new Point2d(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(final Point2d point) {
        return new Vector2d(this.center, point).module() <= this.radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(final Point2d position) {
        this.center = position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2d getPosition() {
        return this.center;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersect(final Shape2d shape) {
        if (shape instanceof Circle2d) {
            return new Vector2d(this.center, shape.getPosition()).module() <= this.radius
                    + ((Circle2d) shape).getRadius();
        }
        return false;
    }

    /**
     * @return The radius of the circle.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Circle2d [radius=" + radius + ", center=" + center + "]";
    }

    /**
     * @return A point representing the center of the circle.
     */
    @Override
    public Point2d getCenter() {
        return this.getPosition();
    }

}
