package io.github.agentseek.core

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.view.Renderer

/**
 * Shorthand build to construct [GameObject]s
 */
class GameObjectBuilder {
    /**
     * This object's current position.
     */
    var position: Point2d = Point2d.origin()

    /**
     * Gets the [List] of [Component]s added to this object.
     */
    var components: MutableList<Component> = mutableListOf()

    /**
     * This object's [HitBox].
     */
    var hitBox: HitBox = CircleHitBox(GameObjectImpl.DEFAULT_HITBOX_RADIUS)

    /**
     * The GameObject graphical appearance
     */
    var renderer: Renderer = TODO()

    fun position(position: Point2d): GameObjectBuilder {
        this.position = position
        return this
    }

    fun position(x: Int, y: Int): GameObjectBuilder {
        this.position = Point2d(x.toDouble(), y.toDouble())
        return this
    }


    fun with(component: Component): GameObjectBuilder {
        components.add(component)
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
        val gameObject = GameObjectImpl(renderer, hitBox)
        gameObject.position = position
        components.forEach { gameObject.addComponent(it) }
        return gameObject
    }
}
