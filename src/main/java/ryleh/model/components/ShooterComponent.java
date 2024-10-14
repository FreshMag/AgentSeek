package ryleh.model.components;

import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.controller.Entity;
import ryleh.model.GameObject;
import ryleh.model.World;

/**
 * A class that provides the Component for shooter enemy type.
 */
public class ShooterComponent extends AbstractComponent {
    private final ShootingComponent shooting;
    private Point2d position;
    /**
     * Speed at which bullet entities move.
     */
    private static final double BULLET_SPEED = 0.25;
    private final Entity player;

    /**
     * Constructor method.
     * 
     * @param world  World instance.
     * @param player Entity instance.
     */
    public ShooterComponent(final World world, final Entity player) {
        super(world);
        this.position = new Point2d(0, 0);
        this.player = player;
        shooting = new ShootingComponent(world, 0.75);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        shooting.onAdded(object);
        this.position = object.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double dt) {
        shoot();
    }

    /**
     * A method that calls shoot method from ShootingComponent if the shooter is
     * able to shoot.
     */
    public void shoot() {
        shooting.shoot(this.getDirection());
    }

    /**
     * A method to get the direction towards the player.
     * 
     * @return a V2d value representing a direction.
     */
    public Vector2d getDirection() {
        return new Vector2d(player.getGameObject().getPosition().getX(), player.getGameObject().getPosition().getY())
                .sub(new Vector2d(this.position.getX(), this.position.getY())).getNormalized().mulLocal(BULLET_SPEED);
    }
}
