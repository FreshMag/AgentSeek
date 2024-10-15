package io.github.agentseek.controller.events

import io.github.agentseek.controller.Entity
import io.github.agentseek.controller.core.GameState
import io.github.agentseek.controller.events.Event
import io.github.agentseek.model.GameObject
import java.util.*

/**
 * This class manages a RemoveEntity Event and implements Event interface.
 */
class RemoveEntityEvent(target: GameObject) : Event {
    private val target: GameObject = target

    /**
     * {@inheritDoc} Removes the target and decreases the counter of enemies if
     * possible
     */
    override fun handle(state: GameState) {
        this.removeEntity(target, state)
        if (isEnemy(target.type)) {
            state.getLevelHandler().decreaseEnemies()
        }
    }

    /**
     * Removes the target from the list of entities.
     *
     * @param target Game object to be removed
     * @param state  The actual state of the game
     */
    private fun removeEntity(target: GameObject, state: GameState) {
        val removable: Optional<Entity> = state.getEntities().stream().filter { e ->
            e.getGameObject().equals(target)
        }
            .findAny()
        if (removable.isPresent) {
            state.removeEntity(removable.get())
        }
    }

    /**
     * Check if the target is an enemy or not.
     *
     * @param type
     * @return
     */
    private fun isEnemy(type: Type): Boolean {
        return type.equals(Type.ENEMY_DRUNK) || type.equals(Type.ENEMY_DRUNKSPINNER) || type.equals(Type.ENEMY_LURKER)
                || type.equals(Type.ENEMY_SHOOTER) || type.equals(Type.ENEMY_SPINNER)
    }
}
