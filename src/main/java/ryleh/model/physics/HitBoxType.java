package ryleh.model.physics;
/**
 * Enum used to determine hitbox radius of different entities.
 */
public enum HitBoxType {
    /** Player standard hitbox.*/
    PLAYER(70),
    /** Bullet standard hitbox.*/
    BULLET(5),
    /** Rock standard hitbox.*/
    ROCK(57),
    /** Item standard hitbox.*/
    ITEM(30),
    /** Fire standard hitbox.*/
    FIRE(40),
    /** Door standard hitbox.*/
    DOOR(75),
    /** Enemy standard hitbox.*/
    ENEMY(55);
    private  int boxRadius;
    HitBoxType(final int radius) {
        boxRadius = radius;
    }
    /**
     * Gets the radius value.
     * @return int value.
     */
    public int getBoxRadius() {
        return boxRadius;
    }
}
