package ryleh.model.components;

import java.util.Optional;
import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.controller.Entity;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.World;

/**
 * A class that provides the Component for lurker enemy type.
 */
public class LurkerComponent extends AbstractComponent {
    private long adjustDirectionTimer = System.currentTimeMillis();
    private static final long ADJUST_DELAY = 250;
    private static final int MOVE_SPEED = 50;
    private static final double TPF = 0.016;
    private Point2d position;
    private Vector2d velocity;
    private final GameObject player;

    /**
     * Constructor method.
     * 
     * @param world  World instance.
     * @param entity Entity instance.
     */
    public LurkerComponent(final World world, final Entity entity) {
        super(world);
        player = entity.getGameObject();
        this.position = new Point2d(0, 0);
        this.velocity = new Vector2d(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        adjustVelocity();
        super.onAdded(object);
        this.position = object.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
        move();
        this.checkScreenBounds();
    }

    /**
     * Generates new position to move to.
     */
    private void move() {
        if (System.currentTimeMillis() - adjustDirectionTimer >= ADJUST_DELAY) {
            adjustVelocity();
            adjustDirectionTimer = System.currentTimeMillis();
        }
        this.position.setX(this.position.getX() + this.velocity.getX());
        this.position.setY(this.position.getY() + this.velocity.getY());
        super.getObject().setPosition(this.position);
    }

    /**
     * Checks game world bounds or collision with a rock and if true bounces back.
     */
    private void checkScreenBounds() {
        if (super.getObject().getHitBox().isOutOfBounds(super.getWorld().getBounds()) || this.isCollidingWithRock()) {
            this.velocity.setX(-this.velocity.getX());
            this.velocity.setY(-this.velocity.getY());
        }
    }

    /**
     * Adjusts velocity vector to follow player direction.
     */
    private void adjustVelocity() {
        final Vector2d directionToPlayer = new Vector2d(player.getPosition(), this.position).getNormalized()
                .multiply(MOVE_SPEED);
        velocity = velocity.addLocal(directionToPlayer).multiply(TPF);
    }

    /**
     * Checks if the GameObject collided with a rock.
     * 
     * @return boolean value of collision check.
     */
    private boolean isCollidingWithRock() {
        final Optional<GameObject> colliding = super.getWorld().getGameObjects().stream()
                .filter(obj -> obj.getType().equals(Type.ROCK))
                .filter(obj -> obj.getHitBox().isCollidingWith(super.getObject().getHitBox())).findFirst();
        return colliding.isPresent();
    }
}
