package ryleh.model.physics;

import ryleh.common.Vector2d;
/**
 * This enum is used to determine the direction.
 */
public enum Direction {
    /** Up direction. Directed to negative values of y-axis */
    UP(new Vector2d(0, -1)),
    /** Down direction. Directed to positive values of y-axis */
    DOWN(new Vector2d(0, 1)), 
    /** Left direction. Directed to negative values of x-axis */
    LEFT(new Vector2d(-1, 0)), 
    /** Right direction. Directed to positive values of x-axis */
    RIGHT(new Vector2d(1, 0)), 
    /** Absence of movement. */
    IDLE(new Vector2d(0, 0));

    private Vector2d vector;
    /** Creates a direction given the vector.*/
    Direction(final Vector2d point) {
        this.setVector(point);
    }
    /** Gets the vector representing this direction. 
     * @return Vector of this direction.
    */
    public Vector2d getVector() {
        return vector;
    }
    /** Sets this direction's vector.
     * @param vector Vector to be set.
    */
    public void setVector(final Vector2d vector) {
        this.vector = vector;
    }
}
