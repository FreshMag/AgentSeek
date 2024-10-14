package ryleh.controller.events;

import java.util.Random;

import ryleh.controller.core.GameState;
import ryleh.model.Type;
import ryleh.model.items.FireSpeedItem;
import ryleh.model.items.HealItem;
import ryleh.model.items.Item;
import ryleh.model.items.MaxHealthItem;
import ryleh.view.graphics.other.ItemGraphicComponent;
/**
 * This class manages an ItemPickUp Event and implements Event interface.
 */
public class ItemPickUpEvent implements Event {
    /**
     * {@inheritDoc} Starts the opening animation of the item
     */
    @Override
    public void handle(final GameState state) {
        randomItem(state);
        ((ItemGraphicComponent) state.getEntityByType(Type.ITEM).get().getView()).setAnimPlayed();
    }

    /**
     * Method to generate a random buff with pseudo probability.
     */
    private void randomItem(final GameState state) {
        Item item = new HealItem();
        final Random random = new Random();
        switch (random.nextInt(3)) {
        case 0:
            item = new HealItem();
            state.getView().getGameUi().getItemPickUp().setText("Item effect: health up!");
            break;
        case 1:
            item = new MaxHealthItem();
            state.getView().getGameUi().getItemPickUp().setText("Item effect: max health up!");
            break;
        case 2:
            item = new FireSpeedItem();
            state.getView().getGameUi().getItemPickUp().setText("Item effect: atk speed up!");
            break;
        default:
            break;
        }
        state.getView().getGameUi().playFt();
        item.apply(state);
    }
}
