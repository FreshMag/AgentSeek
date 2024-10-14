package ryleh.model.items;

import ryleh.controller.core.GameState;
/**
 * Interface of an Item. Classes that implement this interface consist in various bonuses that the player can obtain.
 */
public interface Item {

    /**
     * Performs some actions depending on the Item type.
     * @param state
     */
    void apply(GameState state);
}
