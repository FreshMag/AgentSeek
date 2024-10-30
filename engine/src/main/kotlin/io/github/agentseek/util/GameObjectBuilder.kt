package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.view.EmptyRenderer
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World

/**
 * Shorthand build to construct [GameObject]s
 */
class GameObjectBuilder(private val world: World) {
    /**
     * This object's current position.
     */
    var position: Point2d = Point2d.origin()

    /**
     * Name for the game object
     */
    var name: String = ""

    /**
     * Gets the [List] of [Component]s added to this object.
     */
    private var componentSetters: MutableList<(GameObject) -> Component> = mutableListOf()

    /**
     * This object's [RigidBody].
     */
    private var rigidBodySetter: (GameObject) -> RigidBody = {
        RigidBody.RectangleRigidBody(it, GameObject.DEFAULT_SIZE, GameObject.DEFAULT_SIZE)
    }

    /**
     * The GameObject graphical appearance
     */
    private var renderer: Renderer<*> = EmptyRenderer()

    fun position(position: Point2d): GameObjectBuilder {
        this.position = position
        return this
    }

    fun position(x: Double, y: Double): GameObjectBuilder {
        this.position = Point2d(x, y)
        return this
    }

    fun position(x: Int, y: Int): GameObjectBuilder {
        this.position = Point2d(x.toDouble(), y.toDouble())
        return this
    }

    fun with(componentSetter: (GameObject) -> Component): GameObjectBuilder {
        componentSetters.add(componentSetter)
        return this
    }

    fun <T> renderer(renderer: Renderer<T>): GameObjectBuilder {
        this.renderer = renderer
        return this
    }

    fun rigidBody(rigidBodySetter: (GameObject) -> RigidBody): GameObjectBuilder {
        this.rigidBodySetter = rigidBodySetter
        return this
    }

    fun name(name: String): GameObjectBuilder {
        this.name = name
        return this
    }

    @Throws(IllegalStateException::class)
    fun build(): GameObject {
        val gameObject = GameObject(renderer, world)
        gameObject.rigidBody = rigidBodySetter(gameObject)
        gameObject.position = position
        gameObject.name = name
        componentSetters.forEach { gameObject.addComponent(it(gameObject)) }
        return gameObject
    }

    fun buildAndAddToWorld() {
        world.addGameObject(build())
    }
}
