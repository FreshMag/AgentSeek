package io.github.agentseek.components

import io.github.agentseek.core.GameObject
import io.github.agentseek.world.World
import io.github.agentseek.core.isPlayer

/**
 * Abstract implementation of Component. All components should extend this class.
 */
abstract class AbstractComponent(
    /**
     * The World where this component is instantiated.
     */
    @JvmField val world: World
) : Component {
    /**
     * ID generated by the world at the moment this Component is instantiated.
     */
    private var id: String = world.generateId("component")

    /**
     * The Object to which this component is added.
     */
    lateinit var gameObject: GameObject
        private set

    override fun onAdded(gameObject: GameObject) {
        this.gameObject = gameObject
    }

    protected val player: GameObject?
        get() = world.gameObjects.firstOrNull { it.isPlayer() }

    abstract override fun onUpdate(deltaTime: Double)

    final override fun toString(): String {
        return "${this.javaClass.simpleName} [id=$id]"
    }
}
