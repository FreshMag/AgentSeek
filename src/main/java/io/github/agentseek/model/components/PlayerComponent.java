package ryleh.model.components;

import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.World;
import ryleh.model.physics.Direction;
/**
 * Main component of a Game object of type PLAYER. Specifies how player's movement works.
 */
public class PlayerComponent extends AbstractComponent {
    private static final double TIME_MULTIPLIER = 0.001;
    private Vector2d velocity;
    private Point2d position;
    private final int speed;
    private Direction direction;
    private Direction lastDirection;
    private Direction blocked;
    private Point2d lastPos;

    public PlayerComponent(final World world, final int speed) {
        super(world);
        this.position = new Point2d(0, 0);
        this.velocity = new Vector2d(0, 0);
        this.lastPos = new Point2d(0, 0);
        this.speed = speed;
        this.direction = Direction.IDLE;
        this.lastDirection = Direction.DOWN;
        this.blocked = Direction.IDLE;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        this.position = object.getPosition();
        this.move(0);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double dt) {
        if (this.canMove() && !this.blocked.equals(this.direction)) {
            move(dt);
        } else {
            this.blocked = this.direction;
            resetPos();
        }
    }
    /**
     * Moves the player.
     * @param deltaTime Time elapsed since last update.
     */
    private void move(final double deltaTime) {
        lastPos = new Point2d(this.position.getX(), this.position.getY());
        this.position = this.position.sum(velocity.multiply(TIME_MULTIPLIER * deltaTime));
        super.getObject().setPosition(this.position);
    }
    /**
     * Resets player's position if player has done an illegal movement. 
     */
    private void resetPos() {
        this.position = lastPos;
        this.setVelocity(new Vector2d(0, 0));
        this.direction = Direction.IDLE;
        super.getObject().setPosition(this.position);
    }
    /**
     * Checks if next movement is legal.
     * @return True if nex next movement is legal.
     */
    private boolean canMove() {
        return !(super.getObject().getHitBox().isOutOfBounds(super.getWorld().getBounds())
                || super.getWorld().getGameObjects().stream().filter(i -> i.getType().equals(Type.ROCK))
                        .anyMatch(r -> super.getObject().getHitBox().isCollidingWith(r.getHitBox())));
    }
    /**
     * Sets player's current velocity.
     * @param velocity Velocity given to the player.
     */
    public void setVelocity(final Vector2d velocity) {
        this.velocity = velocity.multiply(speed);
    }
    /**
     * Sets player's direction.
     * @param direction
     */
    public void setDirection(final Direction direction) {
        if (!this.direction.equals(Direction.IDLE)) {
            this.lastDirection = this.direction;
        }
        this.direction = direction;
    }
    /**
     * Gets direction of last legal movement made by the player.
     * @return Direction.
     */
    public Direction getLastDirection() {
        return this.lastDirection;
    }
    /**
     * Gets current direction of player.
     * @return Direction of the player.
     */
    public Direction getDirection() {
        return this.direction;
    }
    /**
     * Gets current blocked direction. If direction is IDLE player can move in all directions.
     * @return Player's blocked Direction.
     */
    public Direction getBlocked() {
        return this.blocked;
    }
    /**
     * Resets player's blocked direction, setting it to IDLE.
     */
    public void resetBlocked() {
        this.blocked = Direction.IDLE;
    }

    /**
     * Sets player position. NOTE: this is a primitive method, no way of checking if it is a legal position.
     * @param position Position of the player to be set.
     */
    public void setPosition(final Point2d position) {
        this.lastPos = position;
        this.position = position;
        super.getObject().setPosition(this.position);
    }
}
