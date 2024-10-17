package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.events.Event
import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * Abstract implementation of Component. All components should extend this class.
 * [world] world
 */
abstract class AbstractComponent(
    final override val gameObject: GameObject
) : Component {
    val world: World = gameObject.world
    /**
     * ID generated by the world at the moment this Component is instantiated.
     */
    private var id: String = world.generateId("component")

    override fun init() {}

    protected val player: GameObject?
        get() = world.gameObjects.firstOrNull { it.isPlayer() }

    override fun onUpdate(deltaTime: Duration) {}

    override fun onRemoved() {}

    override fun notifyEvent(event: Event) {
        gameObject.notifyEvent(event)
    }

    final override fun toString(): String {
        return "${this.javaClass.simpleName} [id=$id]"
    }

}
