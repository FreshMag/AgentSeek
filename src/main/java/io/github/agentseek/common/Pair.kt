package io.github.agentseek.common

/**
 * A standard generic Pair<X></X>,Y>, with getters, hashCode, equals, and toString.
 * well implemented.
 *
 * @param <X> X type.
 * @param <Y> Y type.
</Y></X> */
class Pair<X, Y>(var x: X, var y: Y) {

    /**
     * Method that assigns a hashcode to Pair instance.
     *
     * @return int int hashcode value.
     */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + (x?.hashCode() ?: 0)
        result = prime * result + (y?.hashCode() ?: 0)
        return result
    }

    /**
     * Method that checks if two Pair instance and other object is equal.
     *
     * @return boolean boolean value.
     */
    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
        val other = obj as Pair<*, *>
        if (x == null) {
            if (other.x != null) {
                return false
            }
        } else if (x != other.x) {
            return false
        }
        if (y == null) {
            if (other.y != null) {
                return false
            }
        } else if (y != other.y) {
            return false
        }
        return true
    }

    /**
     * Method that converts Pair instance to String representation.
     *
     * @return String String value.
     */
    override fun toString(): String {
        return "Pair [x=$x, y=$y]"
    }
}

