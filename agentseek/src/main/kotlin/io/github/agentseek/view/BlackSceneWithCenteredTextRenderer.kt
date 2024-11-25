package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.utilities.Rendering.fillScreenWithTitleAndSubtitle
import java.awt.Color
import java.awt.Graphics2D

/**
 * A renderer that renders a black scene with centered text.
 */
class BlackSceneWithCenteredTextRenderer(override val layer: Layer = Layer.GENERIC) :
    Renderer<Graphics2D> {
    override fun render(
        gameObject: GameObject,
        renderingContext: RenderingContext<Graphics2D>?
    ) {
        renderingContext?.fillScreenWithTitleAndSubtitle(
            Color.BLACK,
            "Game Over",
            "Click anywhere to exit",
            Color.WHITE
        )
    }
}