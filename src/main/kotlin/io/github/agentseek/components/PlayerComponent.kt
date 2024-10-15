package io.github.agentseek.components

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Type
import io.github.agentseek.physics.Direction
import io.github.agentseek.world.World

/**
 * Main component of a Game object of type PLAYER. Specifies how player's movement works.
 */
class PlayerComponent(world: World, private val speed: Int) : AbstractComponent(world) {
    private var velocity: Vector2d = Vector2d.zero()
    private var lastPos: Point2d = Point2d.origin()
    var direction: Direction = Direction.IDLE
        set(value) {
            if (field != Direction.IDLE) {
                this.lastDirection = field
            }
            field = value
        }
    private var lastDirection: Direction = Direction.IDLE
    private var blocked: Direction = Direction.IDLE

    override fun onAdded(gameObject: GameObject) {
        super.onAdded(gameObject)
        this.move(0.0)
    }

    override fun onUpdate(deltaTime: Double) {
        if (this.canMove() && blocked != this.direction) {
            move(deltaTime)
        } else {
            this.blocked = this.direction
            resetPos()
        }
    }

    /**
     * Moves the player. [deltaTime] is the time elapsed since last update.
     */
    private fun move(deltaTime: Double) {
        lastPos = gameObject.position
        gameObject.position += (velocity * (TIME_MULTIPLIER * deltaTime))
    }

    /**
     * Resets player's position if player has done an illegal movement.
     */
    private fun resetPos() {
        gameObject.position = lastPos
        velocity = Vector2d.zero()
        this.direction = Direction.IDLE
    }

    /**
     * Checks if next movement is legal.
     */
    private fun canMove(): Boolean =
        !(gameObject.hitBox.isOutOfBounds(world.bounds)
                || world.gameObjects.filter { it.type == Type.ROCK }
            .any { gameObject.hitBox.isCollidingWith(it.hitBox) })

    /**
     * Gets current blocked direction. If direction is IDLE player can move in all directions.
     * @return Player's blocked Direction.
     */
    fun getBlocked(): Direction {
        return this.blocked
    }

    /**
     * Resets player's blocked direction, setting it to IDLE.
     */
    fun resetBlocked() {
        this.blocked = Direction.IDLE
    }

    companion object {
        private const val TIME_MULTIPLIER = 0.001
    }
}
