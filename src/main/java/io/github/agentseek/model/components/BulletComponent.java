package ryleh.model.components;

import java.util.Optional;

import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.controller.events.EnemyCollisionEvent;
import ryleh.controller.events.RemoveEntityEvent;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.World;
/**
 * Component that describes bullet's behavior in the game.
 */
public class BulletComponent extends AbstractComponent {
    /**
     * Position of this bullet.
     */
    private Point2d position;
    /**
     * Default speed of bullets.
     */
    private static final int SPEED = 10;
    /**
     * Factor to adjust time measure. 
     */
    private static final double TIME_MULTIPLIER = 0.1;
    private final Vector2d velocity;
    /**
     * Instantiate bullet in a World, given origin and direction.
     * @param world
     * @param origin
     * @param direction
     */
    public BulletComponent(final World world, final Point2d origin, final Vector2d direction) {
        super(world);
        this.position = origin;
        this.velocity = direction.multiply(SPEED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        object.setPosition(this.position);
    }

    /**
     * {@inheritDoc}
     */
    public void onUpdate(final double dt) {
        move(dt);
        checkCollision();
    }

    private void checkCollision() {
        Optional<GameObject> colliding;
        if (!super.getObject().getType().equals(Type.PLAYER_BULLET)) {
            colliding = this.checkPlayerCollision();
        } else {
            colliding = this.checkEnemyCollisiom();
        }
        if (colliding.isPresent()) {
            super.getWorld().notifyWorldEvent(new EnemyCollisionEvent(colliding.get()));
        }
        if (colliding.isPresent() || super.getObject().getHitBox().isOutOfBounds(super.getWorld().getBounds())) {
            super.getWorld().notifyWorldEvent(new RemoveEntityEvent(super.getObject()));
        }
    }

    /**
     * Check if the bullet of an enemy is colliding with player or with an obstacle.
     * 
     * @return An Optional that represents the game object which is colliding with
     *         the current bullet
     */
    private Optional<GameObject> checkPlayerCollision() {
        return super.getWorld().getGameObjects().stream()
                .filter(obj -> obj.getType().equals(Type.PLAYER) || obj.getType().equals(Type.ROCK)
                        || obj.getType().equals(Type.ITEM))
                .filter(obj -> obj.getHitBox().isCollidingWith(super.getObject().getHitBox())).findFirst();
    }

    /**
     * Check if the bullet of an enemy is colliding with enemies or with an
     * obstacle.
     * 
     * @return n Optional that represents the game object which is colliding with
     *         the current bullet
     */
    private Optional<GameObject> checkEnemyCollisiom() {
        return super.getWorld().getGameObjects().stream()
                .filter(obj -> obj.getType().equals(Type.ENEMY_DRUNK) || obj.getType().equals(Type.ENEMY_DRUNKSPINNER)
                        || obj.getType().equals(Type.ENEMY_LURKER) || obj.getType().equals(Type.ENEMY_SHOOTER)
                        || obj.getType().equals(Type.ENEMY_SPINNER) || obj.getType().equals(Type.ROCK)
                        || obj.getType().equals(Type.ITEM))
                .filter(obj -> obj.getHitBox().isCollidingWith(super.getObject().getHitBox())).findFirst();
    }
    /**
     * This method moves the bullet with a translation based on velocity and delta time.
     * @param deltaTime
     */
    public void move(final double deltaTime) {
        this.position = this.position.sum(velocity.multiply(deltaTime * TIME_MULTIPLIER));
        super.getObject().setPosition(this.position);
    }

}
