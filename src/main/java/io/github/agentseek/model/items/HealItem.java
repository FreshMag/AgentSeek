package ryleh.model.items;

import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.model.components.HealthIntComponent;
/**
 *This type of item heals player's hps by the maximum hps.
 */
public class HealItem implements Item {
    /**
     * {@inheritDoc}
     * 
     * Total recover of the player
     */
    @Override
    public void apply(final GameState state) {
        ((HealthIntComponent) state.getPlayer().getGameObject().getComponent(HealthIntComponent.class).get())
            .setCurrentHp(((HealthIntComponent) state.getPlayer().getGameObject().getComponent(HealthIntComponent.class).get()).getMaxHp());
        GameEngine.runDebugger(() -> System.out.println("heal"));
    }

}
