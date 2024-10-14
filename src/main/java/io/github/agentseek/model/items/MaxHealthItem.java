package ryleh.model.items;

import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.model.components.HealthIntComponent;
/**
 * This type of Item augments player's max hp by one.
 */
public class MaxHealthItem implements Item {

    /**
     * {@inheritDoc}
     * Increases the max health of the player
     */
    @Override
    public void apply(final GameState state) {
        ((HealthIntComponent) state.getPlayer().getGameObject().getComponent(HealthIntComponent.class).get()).increaseMaxHp(1);
        GameEngine.runDebugger(() -> System.out.println("max heal"));
    }
}
