package ryleh.common;

/**
 * A two-dimensional point.
 */
public class Point2d {
    private double x;
    private double y;

    public Point2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sums to this point a vector.
     * 
     * @param v Vector to be summed to the point.
     * @return A new P2d representing the sum between this point and vector.
     */
    public Point2d sum(final Vector2d v) {
        return new Point2d(x + v.getX(), y + v.getY());
    }

    /**
     * Vector from this point to another.
     * 
     * @param point Second point
     * @return Vector from this point to the point given in input.
     */
    public Vector2d sub(final Point2d point) {
        return new Vector2d(x - point.getX(), y - point.getY());
    }

    /**
     * The x coordinate.
     * 
     * @return X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x coordinate.
     * 
     * @param x coordinate to be set.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * The y coordinate.
     * 
     * @return Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets y coordinate.
     * 
     * @param y coordinate to be set.
     */
    public void setY(final double y) {
        this.y = y;
    }
}
