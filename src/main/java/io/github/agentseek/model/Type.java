package ryleh.model;
/**
 * This enum represents the type of each game object. Each object added to the world belong to one of these types.
 */
public enum Type {
    /** Player entity type. */
    PLAYER,
    /** Rock entity type. */
    ROCK,
    /** Door entity type. */
    DOOR,
    /** Drunk enemy entity type. */
    ENEMY_DRUNK,
    /** Shooter enemy entity type. */
    ENEMY_SHOOTER,
    /** Item entity type. */
    ITEM,
    /** Player's bullet entity type. */
    PLAYER_BULLET,
    /** Enemy's bullet entity type. */
    ENEMY_BULLET,
    /** Lurker enemy entity type. */
    ENEMY_LURKER,
    /** Spinner enemy entity type. */
    ENEMY_SPINNER, 
    /** DrunkSpinner enemy entity type. */
    ENEMY_DRUNKSPINNER,
    /** Fire entity type. */
    FIRE;
}
