package io.github.agentseek.controller

import io.github.agentseek.view.GraphicComponent

/**
 * An interface to define an Entity type.
 */
interface Entity {
    /**
     * The graphical appearance of this entity.
     */
    var view: GraphicComponent

    /**
     * The [GameObject] (model) related to this entity.
     */
    var gameObject: GameObject
}
