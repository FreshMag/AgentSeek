package ryleh.controller.events;

import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.model.GameObject;
import ryleh.model.Type;
import ryleh.model.components.HealthIntComponent;
/**
 * This class manages an EnemyCollision Event and implements Event interface.
 */
public class EnemyCollisionEvent implements Event {

    private final GameObject target;

    /**
     * Constructor for a collision event.
     * 
     * @param target The target of the event.
     */
    public EnemyCollisionEvent(final GameObject target) {
        this.target = target;
    }

    /**
     * {@inheritDoc} Decreases actual target health (if target is "PLAYER", health
     * will be decreased only in release mode).
     */
    @Override
    public void handle(final GameState state) {
        if (this.target.getComponent(HealthIntComponent.class).isPresent()
                && !(this.target.getType().equals(Type.PLAYER) && GameEngine.isDeveloper())) {
            ((HealthIntComponent) this.target.getComponent(HealthIntComponent.class).get()).damage(1);
        }
    }
}
