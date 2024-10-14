package ryleh.model.components;

import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.model.GameObject;
import ryleh.model.World;

/**
 * A class that provides the Component for spinner enemy type.
 */
public class SpinnerComponent extends AbstractComponent {
    private final ShootingComponent shooting;
    private Point2d position;
    /**
     * Speed at which bullet entities move.
     */
    private static final double BULLET_SPEED = 0.30;
    /**
     * Rate at which rotation angle changes.
     */
    private static final double ANGLE_INCREASE = Math.PI / 40;
    private double angle;

    /**
     * Constructor method.
     * 
     * @param world World instance.
     */
    public SpinnerComponent(final World world) {
        super(world);
        this.shooting = new ShootingComponent(world, 1);
        this.position = new Point2d(0, 0);
        this.angle = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        this.shooting.onAdded(object);
        this.position = object.getPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double dt) {
        shoot();
        checkAngle();
    }

    /**
     * A method that calls shoot method from ShootingComponent towards a direction
     * given internal rotation angle value if the shooter is able to shoot.
     */
    private void shoot() {
        final Vector2d direction = new Vector2d(
                this.position.getX() * Math.cos(this.angle) - position.getY() * Math.sin(this.angle),
                position.getX() * Math.sin(this.angle) + position.getY() * Math.cos(this.angle)).getNormalized()
                        .mulLocal(BULLET_SPEED);
        shooting.shoot(direction);
    }

    /**
     * Method that increases rotation angle value and resets it if angle > 360°.
     */
    private void checkAngle() {
        angle = angle + ANGLE_INCREASE;
        if (angle >= Math.PI * 2) {
            angle = 0;
        }
    }
}
