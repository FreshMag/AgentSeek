package ryleh.model.components;

import ryleh.common.Point2d;
import ryleh.common.Timer;
import ryleh.common.TimerImpl;
import ryleh.common.Vector2d;
import ryleh.controller.events.BulletSpawnEvent;
import ryleh.model.World;
/**
 * A Component that lets you shoot.
 */
public class ShootingComponent extends AbstractComponent {

    private double attackSpeed;
    private final Timer timer;
    /**
     * @param world 
     * @param attackSpeed Number of shoots per second
     */
    public ShootingComponent(final World world, final double attackSpeed) {
        super(world);
        this.attackSpeed = attackSpeed;
        timer = new TimerImpl(1000.0 / this.attackSpeed); //Timer class uses milliseconds
        timer.startTimer();
    }
    /**
     * Shoots a bullet at given origin and with the given velocity only if attack speed's timer is elapsed.
     * @param velocity given to the bullet.
     * @param origin of bullet spawning.
     */
    public void shoot(final Vector2d velocity, final Point2d origin) {
        if (timer.isElapsed()) {
            super.getWorld().notifyWorldEvent(new BulletSpawnEvent(super.getObject(), origin, velocity));
            this.timer.startTimer();
        }
    }
    /**
     * Shoot a bullet at game object's position with the given velocity only if attack speed's timer is elapsed.
     * @param velocity
     */
    public void shoot(final Vector2d velocity) {
        if (timer.isElapsed()) {
            super.getWorld().notifyWorldEvent(new BulletSpawnEvent(super.getObject(), 
                    super.getObject().getHitBox().getForm().getCenter(), velocity));
            this.timer.startTimer();
        }
    }
    /**
     * Multiplies attack speed by a factor, scaling it. 
     * @param factor multiplies old attack speed.
     */
    public void multiplyAtkSpeed(final double factor) {
        this.attackSpeed *= factor;
        this.timer.setWaitTime(1000.0 / this.attackSpeed);
    }
    /**
     * Increases attack speed linearly, adding an amount to the number of shoots per second.
     * @param amount number of shoots per second added to the current attack speed.
     */
    public void increaseAtkSpeed(final double amount) {
        this.attackSpeed += amount;
        this.timer.setWaitTime(1000.0 / this.attackSpeed);
    }
    /**
     * Check whether object can shoot or not.
     * @return true if object can shoot.
     */
    public boolean canShoot() {
        return this.timer.isElapsed();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
    }
}
