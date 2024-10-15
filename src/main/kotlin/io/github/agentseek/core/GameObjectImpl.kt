package io.github.agentseek.core

import io.github.agentseek.common.Point2d
import io.github.agentseek.world.World
import io.github.agentseek.components.Component
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.view.Renderer

/**
 * A class that provides the implementation of the interface GameObject,
 * handling the operations related to a GameObject.
 */
class GameObjectImpl(override val type: Type, override var renderer: Renderer = TODO()) : GameObject {
    private var id: String? = null
    override var position: Point2d = Point2d(0.0, 0.0)
        set(value) {
            hitBox.form.position = value
            field = value
        }
    override val hitBox: HitBox = CircleHitBox(DEFAULT_HITBOX_RADIUS)
    override var components: List<Component> = ArrayList()

    override fun onAdded(world: World) {
        this.id = world.generateId("gameObject")
        hitBox.form.position = position
    }

    override fun onUpdate(deltaTime: Double) {
        components.forEach { it.onUpdate(deltaTime) }
        renderer.render(this)
    }

    override fun addComponent(component: Component) {
        check(!components.stream().anyMatch { component.javaClass.isInstance(it) })
        components += component
        component.onAdded(this)
    }

    override fun toString(): String {
        return "GameObjectImpl [id=$id, type=$type]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameObjectImpl

        if (type != other.type) return false
        if (id != other.id) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + position.hashCode()
        return result
    }


    companion object {
        /**
         * The default HitBox radius of a GameObject.
         */
        private const val DEFAULT_HITBOX_RADIUS = 100
    }
}
