package io.github.agentseek.view

import io.github.agentseek.components.common.Config
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * A JPanel used to display the game view. It receives a rendering function that will be called every time the panel
 * need to update the game view.
 */
class GameViewPanel(size: Dimension, private val rendering: (Graphics2D) -> Unit) : JPanel() {
    init {
        layout = BorderLayout()
        this.preferredSize = Dimension(size)
        this.add(MainGraphicContainer(), BorderLayout.CENTER)
        this.background = Config.Rendering.backgroundColor
        revalidate()
        repaint()
    }

    /**
     * A private inner class that extends [JComponent]. It is used to draw the game view.
     */
    private inner class MainGraphicContainer : JComponent() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val graph2 = g as Graphics2D

            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            rendering(graph2)
        }
    }
}