package io.github.agentseek.model.components

import io.github.agentseek.controller.GameObject

/**
 * A Component can be added to a GameObject to specify some sort of additional behavior.
 */
interface Component {
    /**
     * This method is called when this component is added to a [GameObject].
     */
    fun onAdded(gameObject: GameObject)

    /**
     * This method is called once every update of GameState. [deltaTime] is the time elapsed since last update.
     */
    fun onUpdate(deltaTime: Double)
}
