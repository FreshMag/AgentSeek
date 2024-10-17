package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
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
     * Gets the [List] of [Component]s added to this object.
     */
    private var componentSetters: MutableList<(GameObject) -> Component> = mutableListOf()

    /**
     * This object's [HitBox].
     */
    private var hitBox: HitBox = CircleHitBox(GameObject.DEFAULT_HITBOX_RADIUS)

    /**
     * The GameObject graphical appearance
     */
    private var renderer: Renderer = EmptyRenderer()

    fun position(position: Point2d): GameObjectBuilder {
        this.position = position
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

    fun renderer(renderer: Renderer): GameObjectBuilder {
        this.renderer = renderer
        return this
    }

    fun bbox(hitBox: HitBox): GameObjectBuilder {
        this.hitBox = hitBox
        return this
    }

    @Throws(IllegalStateException::class)
    fun build(): GameObject {
        val gameObject = GameObject(renderer, hitBox, world)
        gameObject.position = position
        componentSetters.forEach { gameObject.addComponent(it(gameObject)) }
        return gameObject
    }
}
