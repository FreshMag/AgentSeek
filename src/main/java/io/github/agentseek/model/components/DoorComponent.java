package ryleh.model.components;

import java.util.Optional;

import ryleh.common.Timer;
import ryleh.common.TimerImpl;
import ryleh.controller.events.NewLevelEvent;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.World;
/**
 * Component used to describe door's behavior.
 */
public class DoorComponent extends AbstractComponent {

    private boolean isCollidable;
    private final Timer timer;

    public DoorComponent(final World world, final int duration) {
        super(world);
        this.isCollidable = false;
        this.timer = new TimerImpl(duration);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final GameObject object) {
        super.onAdded(object);
        timer.startTimer();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
        if (timer.isElapsed() || isCollidable) {
            final Optional<GameObject> colliding = super.getWorld().getGameObjects().stream()
                    .filter(obj -> obj.getType().equals(Type.PLAYER))
                    .filter(obj -> obj.getHitBox().isCollidingWith(super.getObject().getHitBox())).findFirst();
            this.isCollidable = true;
            if (colliding.isPresent()) {
                super.getWorld().notifyWorldEvent(new NewLevelEvent());
            }
        }
    }
    /**
     * Checks if the door is collidable.
     * @return True if the door is collidable.
     */
    public boolean isCollidable() {
        return isCollidable;
    }

}
