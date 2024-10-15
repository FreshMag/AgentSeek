package io.github.agentseek.controller

import io.github.agentseek.common.Point2d
import io.github.agentseek.model.World
import io.github.agentseek.model.components.Component
import io.github.agentseek.model.physics.CircleHitBox
import io.github.agentseek.model.physics.HitBox
import java.util.function.Consumer

/**
 * A class that provides the implementation of the interface GameObject,
 * handling the operations related to a GameObject.
 */
class GameObjectImpl(override val type: Type) : GameObject {
    private var id: String? = null
    override var position: Point2d = Point2d(0.0, 0.0)
        set(value) {
            hitBox.form.setPosition(value)
        }
    override val hitBox: HitBox = CircleHitBox(DEFAULT_HITBOX_RADIUS)
    override var components: List<Component> = ArrayList()

    override fun onAdded(world: World) {
        this.id = world.generateId("gameObject")
        hitBox.form.setPosition(position)
    }

    override fun onUpdate(deltaTime: Double) {
        components.forEach(Consumer { i: Component? -> i!!.onUpdate(deltaTime) })
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
