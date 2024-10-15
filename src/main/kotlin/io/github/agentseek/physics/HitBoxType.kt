package io.github.agentseek.physics

/**
 * Enum used to determine hitbox radius of different entities.
 */
enum class HitBoxType(
    /**
     * Gets the radius value.
     * @return int value.
     */
    val boxRadius: Int
) {
    /** Player standard hitbox. */
    PLAYER(70),

    /** Bullet standard hitbox. */
    BULLET(5),

    /** Rock standard hitbox. */
    ROCK(57),

    /** Item standard hitbox. */
    ITEM(30),

    /** Fire standard hitbox. */
    FIRE(40),

    /** Door standard hitbox. */
    DOOR(75),

    /** Enemy standard hitbox. */
    ENEMY(55)
}
