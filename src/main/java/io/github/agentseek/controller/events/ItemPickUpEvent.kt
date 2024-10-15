package io.github.agentseek.controller.events

import ryleh.controller.core.GameState
import java.util.*

/**
 * This class manages an ItemPickUp Event and implements Event interface.
 */
class ItemPickUpEvent : Event {
    /**
     * {@inheritDoc} Starts the opening animation of the item
     */
    override fun handle(state: GameState) {
        randomItem(state)
        (state.getEntityByType(Type.ITEM).get().getView() as ItemGraphicComponent).setAnimPlayed()
    }

    /**
     * Method to generate a random buff with pseudo probability.
     */
    private fun randomItem(state: GameState) {
        var item: Item = HealItem()
        val random = Random()
        when (random.nextInt(3)) {
            0 -> {
                item = HealItem()
                state.getView().getGameUi().getItemPickUp().setText("Item effect: health up!")
            }

            1 -> {
                item = MaxHealthItem()
                state.getView().getGameUi().getItemPickUp().setText("Item effect: max health up!")
            }

            2 -> {
                item = FireSpeedItem()
                state.getView().getGameUi().getItemPickUp().setText("Item effect: atk speed up!")
            }

            else -> {}
        }
        state.getView().getGameUi().playFt()
        item.apply(state)
    }
}
