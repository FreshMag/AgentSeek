package ryleh.common;

/**
 * 
 * A standard generic Pair<X,Y>, with getters, hashCode, equals, and toString.
 * well implemented.
 * 
 * @param <X> X type.
 * @param <Y> Y type.
 */
public class Pair<X, Y> {
    private X x;
    private Y y;

    /**
     * Constructor method that creates a Pair instance.
     * 
     * @param x X value.
     * @param y Y value.
     */
    public Pair(final X x, final Y y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the value for x.
     * 
     * @param x X value.
     */
    public void setX(final X x) {
        this.x = x;
    }

    /**
     * Sets the value for y.
     * 
     * @param y Y value.
     */
    public void setY(final Y y) {
        this.y = y;
    }

    /**
     * Gets the value for x.
     * 
     * @return X value.
     */
    public X getX() {
        return x;
    }

    /**
     * Gets the value for y.
     * 
     * @return Y value.
     */
    public Y getY() {
        return y;
    }

    /**
     * Method that assigns a hashcode to Pair instance.
     * 
     * @return int int hashcode value.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }

    /**
     * Method that checks if two Pair instance and other object is equal.
     * 
     * @return boolean boolean value.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair other = (Pair) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!y.equals(other.y)) {
            return false;
        }
        return true;
    }

    /**
     * Method that converts Pair instance to String representation.
     * 
     * @return String String value.
     */
    @Override
    public String toString() {
        return "Pair [x=" + x + ", y=" + y + "]";
    }
}

