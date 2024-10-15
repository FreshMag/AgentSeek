package io.github.agentseek.physics

import io.github.agentseek.common.Vector2d

/**
 * This enum is used to determine the direction.
 */
enum class Direction(var vector: Vector2d) {
    /** Up direction. Directed to negative values of y-axis  */
    UP(Vector2d(0.0, -1.0)),

    /** Down direction. Directed to positive values of y-axis  */
    DOWN(Vector2d(0.0, 1.0)),

    /** Left direction. Directed to negative values of x-axis  */
    LEFT(Vector2d(-1.0, 0.0)),

    /** Right direction. Directed to positive values of x-axis  */
    RIGHT(Vector2d(1.0, 0.0)),

    /** Absence of movement.  */
    IDLE(Vector2d(0.0, 0.0));

}
