package ryleh.controller.events;

import java.util.ArrayList;
import java.util.List;
import ryleh.controller.core.GameState;
import ryleh.model.components.HealthIntComponent;
import ryleh.view.graphics.PlayerGraphicComponent;
/**
 * This class manages other Event instances and implements EventListener.
 */
public class EventHandler implements EventListener {

    private final GameState gameState;
    private final List<Event> eventQueue;
    private HealthIntComponent comp;

    public EventHandler(final GameState gameState) {
        this.gameState = gameState;
        this.eventQueue = new ArrayList<>();
    }

    /**
     * This method is called once every game loop. It checks all events inside the
     * Event Queue and handles their behavior.
     */
    public void checkEvents() {
        if (!this.eventQueue.isEmpty()) {
            this.eventQueue.forEach(e -> {
                e.handle(this.gameState);
            });
            this.eventQueue.clear();
        }
        this.updateUI();
        this.checkPlayerState();
    }

    /**
     * Check the player state so that the graphic component can apply effects in
     * special cases.
     */
    private void checkPlayerState() {
        final PlayerGraphicComponent playerGraphic = (PlayerGraphicComponent) this.gameState.getPlayer().getView();
        playerGraphic.setInvincible(comp.isImmortal());
    }

    /**
     * Update game UI after event handling.
     */
    private void updateUI() {
        comp = (HealthIntComponent) this.gameState.getPlayer().getGameObject().getComponent(HealthIntComponent.class)
                .get();
        gameState.getView().getGameUi().getLives().setText("Lives: " + comp.getCurrentHp());
        this.gameState.getView().getGameUi().getLevel()
                .setText("Level: " + this.gameState.getLevelHandler().getRoomsCount());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyEvent(final Event e) {
        this.eventQueue.add(e);
    }
}
