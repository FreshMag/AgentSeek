package ryleh.controller.events;

import ryleh.controller.core.GameState;
/**
 * This class manages a GameOver Event and implements Event interface.
 */
public class GameOverEvent implements Event {

    /**
     * {@inheritDoc} Sets the game over view
     */
    @Override
    public void handle(final GameState state) {
        state.callGameOver(false);
    }

}
