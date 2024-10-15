package io.github.agentseek.model.physics

import io.github.agentseek.common.Circle2d
import io.github.agentseek.common.Shape2d
import io.github.agentseek.common.Vector2d

/**
 * This type of hit box uses a Circle to handle geometric intersection.
 */
class CircleHitBox(override val form: Circle2d) : HitBox {

    /**
     * Instantiate a CircleHitBox given the [radius] of his circle.
     */
    constructor(radius: Int) : this(Circle2d(radius))

    override fun isCollidingWith(hitBox: HitBox): Boolean {
        return form.intersect(hitBox.form)
    }

    override fun isOutOfBounds(bounds: Shape2d): Boolean {
        return !(bounds.contains(form.getPosition())
                && bounds.contains(form.getPosition().sum(Vector2d(0.0, form.radius.toDouble())))
                && bounds.contains(form.getPosition().sum(Vector2d(0.0, -form.radius.toDouble())))
                && bounds.contains(form.getPosition().sum(Vector2d(form.radius.toDouble(), 0.0)))
                && bounds.contains(form.getPosition().sum(Vector2d(-form.radius.toDouble(), 0.0))))
    }

    override fun toString(): String {
        return "CircleHitBox [form=$form]"
    }
}
