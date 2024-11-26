package io.github.agentseek.view

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillShape
import java.awt.Graphics2D

/**
 * A renderer for the hearing agent in the game.
 */
class HearingRenderer() : SimpleRenderer() {

    override fun render(
        gameObject: GameObject,
        renderingContext: RenderingContext<Graphics2D>?
    ) {
        super.render(gameObject, renderingContext)
        renderingContext?.fillShape(gameObject.rigidBody.shape, Config.Agents.hearingDefaultColor) ?: return
    }
}