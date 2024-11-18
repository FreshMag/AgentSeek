package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillShape
import java.awt.Color
import java.awt.Graphics2D

class DoorRenderer() : SimpleRenderer() {

    override fun render(
        gameObject: GameObject,
        renderingContext: RenderingContext<Graphics2D>?
    ) {
        renderingContext?.fillShape(gameObject.rigidBody.shape, Color.WHITE) ?: return
    }
}