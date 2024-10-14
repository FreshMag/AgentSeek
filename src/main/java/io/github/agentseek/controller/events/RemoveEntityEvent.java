package ryleh.controller.events;

import java.util.Optional;

import ryleh.controller.Entity;
import ryleh.controller.core.GameState;
import ryleh.model.GameObject;
import ryleh.model.Type;
/**
 * This class manages a RemoveEntity Event and implements Event interface.
 */
public class RemoveEntityEvent implements Event {

    private final GameObject target;

    public RemoveEntityEvent(final GameObject target) {
        this.target = target;
    }

    /**
     * {@inheritDoc} Removes the target and decreases the counter of enemies if
     * possible
     */
    @Override
    public void handle(final GameState state) {
        this.removeEntity(target, state);
        if (isEnemy(this.target.getType())) {
            state.getLevelHandler().decreaseEnemies();
        }
    }

    /**
     * Removes the target from the list of entities.
     * 
     * @param target Game object to be removed
     * @param state  The actual state of the game
     */
    private void removeEntity(final GameObject target, final GameState state) {
        final Optional<Entity> removable = state.getEntities().stream().filter(e -> e.getGameObject().equals(target))
                .findAny();
        if (removable.isPresent()) {
            state.removeEntity(removable.get());
        }
    }

    /**
     * Check if the target is an enemy or not.
     * 
     * @param type
     * @return
     */
    private boolean isEnemy(final Type type) {
        return type.equals(Type.ENEMY_DRUNK) || type.equals(Type.ENEMY_DRUNKSPINNER) || type.equals(Type.ENEMY_LURKER)
                || type.equals(Type.ENEMY_SHOOTER) || type.equals(Type.ENEMY_SPINNER);
    }
}
