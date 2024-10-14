package ryleh.controller.events;

import ryleh.controller.core.GameState;
/**
 * This interface represents an Event and the action performed by one.
 */
public interface Event {
    /**
     * Perform some actions depending on the specified event.
     * 
     * @param state is the current state of the game.
     */
    void handle(GameState state);
}
