package io.github.agentseek.core

import io.github.agentseek.common.Point2d
import io.github.agentseek.world.World
import io.github.agentseek.components.Component
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.view.Renderer
import kotlin.time.Duration

/**
 * A class that provides the implementation of the interface GameObject,
 * handling the operations related to a GameObject.
 */
class GameObjectImpl(
    override var renderer: Renderer = TODO(),
    override val hitBox: HitBox = CircleHitBox(DEFAULT_HITBOX_RADIUS),
    override val world: World
) : GameObject {
    override val id: String = world.generateId("gameObject")
    override var position: Point2d = Point2d(0.0, 0.0)
        set(value) {
            hitBox.form.position = value
            field = value
        }
    override var components: List<Component> = ArrayList()

    init {
        hitBox.form.position = position
    }

    override fun onUpdate(deltaTime: Duration) {
        components.forEach { it.onUpdate(deltaTime) }
        renderer.render(this)
    }

    @Throws(IllegalStateException::class)
    override fun addComponent(component: Component) {
        check(!components.any { component.javaClass.isInstance(it) })
        components += component
        component.init()
    }

    override fun removeComponent(component: Component) {
        component.onRemoved()
        components -= component
    }

    override fun spawn(gameObject: GameObject) {
        world.addGameObject(gameObject)
    }

    override fun delete() {
        world.removeGameObject(this)
    }

    override fun toString(): String {
        return "GameObjectImpl [id=$id]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameObjectImpl

        if (id != other.id) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode() ?: 0
        result = 31 * result + position.hashCode()
        result = 31 * result + components.hashCode()
        return result
    }

    companion object {
        /**
         * The default HitBox radius of a GameObject.
         */
        const val DEFAULT_HITBOX_RADIUS = 100
    }
}
