package ryleh.controller.events;

import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import ryleh.controller.core.GameState;
import ryleh.controller.core.factories.BasicFactory;
import ryleh.model.GameObject;
/**
 * This class manages a BulletSpawn Event and implements Event interface.
 */
public class BulletSpawnEvent implements Event {

    private final Point2d position;
    private final Vector2d velocity;
    private final GameObject target;

    /**
     * Constructor for the spawn of a bullet.
     * 
     * @param spawner  The Game object which has to spawn the bullet
     * @param position Current position of the object
     * @param velocity Velocity of the bullet
     */
    public BulletSpawnEvent(final GameObject spawner, final Point2d position, final Vector2d velocity) {
        this.target = spawner;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(final GameState state) {
        state.addEntity(
                BasicFactory.getInstance().createBullet(state, this.position, this.velocity, this.target.getType()));
    }
}
