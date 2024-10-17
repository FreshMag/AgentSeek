package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.events.Event
import kotlin.time.Duration

/**
 * A Component can be added to a GameObject to specify some sort of additional behavior.
 */
interface Component {
    /**
     * GameObject to which this component is added
     */
    val gameObject: GameObject
    /**
     * This method is called when this component is added to a [GameObject].
     */
    fun init()

    /**
     * This method is called once every update of GameState. [deltaTime] is the time elapsed since last update.
     */
    fun onUpdate(deltaTime: Duration)

    /**
     * This method is called when this component gets removed from its [GameObject].
     * Note: this method gets called also when the [GameObject] gets deleted.
     */
    fun onRemoved()

    /**
     * Notifies an event, using its game object as source
     */
    fun notifyEvent(event: Event)
}
