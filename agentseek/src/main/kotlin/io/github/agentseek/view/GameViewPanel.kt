package io.github.agentseek.view

import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel

class GameViewPanel(size: Dimension, private val rendering: (Graphics2D) -> Unit) : JPanel() {
    init {
        layout = BorderLayout()
        this.preferredSize = Dimension(size)
        this.add(DrawStuff(), BorderLayout.CENTER)
        this.background = Color.WHITE
        revalidate()
        repaint()
    }

    private inner class DrawStuff : JComponent() {
        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            val graph2 = g as Graphics2D

            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            rendering(graph2)
        }
    }
}