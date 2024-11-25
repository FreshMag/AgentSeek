package io.github.agentseek.view

import io.github.agentseek.core.GameObject
import io.github.agentseek.view.gui.GameGui
import java.awt.Color
import java.awt.Graphics2D

class BlackSceneWithCenteredTextRenderer(override val layer: Layer = Layer.GENERIC) :
    Renderer<Graphics2D> {
    override fun render(
        gameObject: GameObject,
        renderingContext: RenderingContext<Graphics2D>?
    ) {
        renderingContext?.let { context ->
            context.render { g2d ->
                val width = GameGui.screenWidth
                val height = GameGui.screenHeight
                val text = "Game Over"
                val font = g2d.font.deriveFont(64f)
                g2d.font = font
                val metrics = g2d.fontMetrics
                val textWidth = metrics.stringWidth(text)
                val textHeight = metrics.height

                // Draw black rectangle
                g2d.color = Color.BLACK
                g2d.fillRect(0, 0, width, height)

                // Draw text
                g2d.color = Color.WHITE
                g2d.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - metrics.descent - 40)
                val subtitle = "Click anywhere to exit"
                val lowerFont = g2d.font.deriveFont(24f)
                g2d.font = lowerFont
                val newMetrics = g2d.fontMetrics
                val subtitleWidth = newMetrics.stringWidth(subtitle)
                val subtitleHeight = newMetrics.height
                g2d.drawString(
                    subtitle,
                    (width - subtitleWidth) / 2,
                    (height + subtitleHeight) / 2 - newMetrics.descent + 40
                )
            }
        }
    }
}