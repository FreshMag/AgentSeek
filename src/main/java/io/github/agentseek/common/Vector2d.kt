package ryleh.common;

/**
 * Two dimensional vector.
 */
public class Vector2d {
    private double x;
    private double y;

    /**
     * Instantiates a vector given x-component and y-component.
     * 
     * @param x X component.
     * @param y Y component.
     */
    public Vector2d(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a vector from two points.
     * 
     * @param origin      Point of application of the vector.
     * @param destination Second point.
     */
    public Vector2d(final Point2d origin, final Point2d destination) {
        this.x = origin.getX() - destination.getX();
        this.y = origin.getY() - destination.getY();
    }

    /**
     * Return the difference between two vectors.
     * 
     * @param vector Vector to be subtracted.
     * @return Difference between the two vectors.
     */
    public Vector2d sub(final Vector2d vector) {
        return new Vector2d(x - vector.getX(), y - vector.getY());
    }

    /**
     * Module of the vector.
     * 
     * @return The module of the vector.
     */
    public double module() {
        return (double) Math.sqrt(x * x + y * y);
    }

    /**
     * Vector normalized.
     * 
     * @return The vector normalized.
     */
    public Vector2d getNormalized() {
        final double module = (double) Math.sqrt(x * x + y * y);
        return new Vector2d(x / module, y / module);
    }

    /**
     * Multiplies this vector by a factor, without changing his state.
     * 
     * @param factor Factor.
     * @return Result of multiplication as a new two-dimensional vector.
     */
    public Vector2d multiply(final double factor) {
        return new Vector2d(x * factor, y * factor);
    }

    /**
     * Generate a new vector given the angle with x-axis.
     * 
     * @param degrees Angle.
     * @return Vector generated.
     */
    public static Vector2d fromAngle(final double degrees) {
        return new Vector2d(GameMath.cosDeg((float) degrees), GameMath.sinDeg((float) degrees));
    }

    /**
     * Multiplies this vector by a factor, changing his state.
     * 
     * @param factor Factor.
     * @return Result of multiplication as a new two-dimensional vector.
     */
    public Vector2d mulLocal(final double factor) {
        this.x *= factor;
        this.y *= factor;
        return new Vector2d(this.x, this.y);
    }

    /**
     * Sum of two vectors. ATTENTION: this vector's state will be changed!
     * 
     * @param vector Vector sum.
     * @return The sum of the two vectors.
     */
    public Vector2d addLocal(final Vector2d vector) {
        this.x += vector.x;
        this.y += vector.y;
        return new Vector2d(this.x, this.y);
    }

    /**
     * X component of the vector.
     * 
     * @return The x component.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets x component.
     * 
     * @param x Component to be set.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Y component of the vector.
     * 
     * @return The y component.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets y component.
     * 
     * @param y Component to be set.
     */
    public void setY(final double y) {
        this.y = y;
    }
}
