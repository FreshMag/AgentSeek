package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillScreenWithTitleAndSubtitle
import java.awt.Color
import java.awt.Graphics2D

/**
 * A renderer that renders a white scene with centered text.
 */
class WhiteSceneWithCenteredTextRenderer(override val layer: Layer = Layer.GENERIC) :
    Renderer<Graphics2D> {
    override fun render(
        gameObject: GameObject,
        renderingContext: RenderingContext<Graphics2D>?
    ) {
        renderingContext?.fillScreenWithTitleAndSubtitle(
            Color.WHITE,
            "Victory!",
            "Click anywhere to exit",
            Color.BLACK
        )
    }
}