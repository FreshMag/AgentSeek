package ryleh.model.components;

import java.util.Optional;
import ryleh.common.GameMath;
import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.World;

/**
 * A class that provides the Component for drunk enemy type.
 */
public class DrunkComponent extends AbstractComponent {
    /**
     * Value used to calculate maximum range value for random angle adjust rate.
     */
    private static final double ANGLE_ADJUST = 0.15;
    /**
     * Drunk enemy movement speed value.
     */
    private static final double MOVE_SPEED = 0.05;
    /**
     * Drunk enemy rotation angle value.
     */
    private static final double ROTATION_ANGLE = 20;
    /**
     * Time per frame multiplier.
     */
    private static final double TPF = 0.01;
    private final Vector2d velocity;
    private Point2d position;
    private final double angleAdjustRate = GameMath.randomInRange(0, ANGLE_ADJUST);
    private double directionAngle = GameMath.toDegrees(GameMath.randomInRange(-1, 1) * Math.PI * 2);
    private final double tx = GameMath.randomInRange(1000, 10_000);

    /**
     * Constructor method.
     * 
     * @param world World instance.
     */
    public DrunkComponent(final World world) {
        super(world);
        this.position = new Point2d(0, 0);
        this.velocity = new Vector2d(0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        this.position = object.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
        adjustAngle();
        move(deltaTime);
        checkScreenBounds();
    }

    /**
     * Adjusts the direction angle.
     */
    private void adjustAngle() {
        if (GameMath.randomBoolean(angleAdjustRate)) {
            // never rotate further than 20 degrees
            directionAngle += Math.min(GameMath.toDegrees(GameMath.smoothNoise(tx) - 0.5), ROTATION_ANGLE);
        }
    }

    /**
     * Generates new direction vector to move to.
     * 
     * @param deltaTime time passed between each frame.
     */
    private void move(final double deltaTime) {
        final Vector2d directionVector = Vector2d.fromAngle(directionAngle).mulLocal(MOVE_SPEED);
        this.velocity.addLocal(directionVector).getNormalized().mulLocal(deltaTime * TPF); // add time per frame value
                                                                                           // to mulLocal
        this.position = this.position.sum(this.velocity);
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
