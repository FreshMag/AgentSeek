package io.github.agentseek.view

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory
import java.awt.*
import java.util.*
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities


object GameGui : JFrame() {
    val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
    const val APP_NAME = "Agent Seek"

    val list: MutableList<Shape> = Collections.synchronizedList(mutableListOf())

    fun startGameGui() {
        name = APP_NAME
        this.add(TestingPanelGraphics(), BorderLayout.CENTER)
        size = screenSize
        preferredSize = screenSize
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        isVisible = true
        GameEngine.loadScene(SceneFactory.replScene().first)
        GameEngine.start()
    }

    class TestingPanelGraphics : JPanel() {
        init {
            layout = BorderLayout()
            this.preferredSize = Dimension(screenSize)
            this.add(DrawStuff(), BorderLayout.CENTER)
            revalidate()
            repaint()
            this.isVisible = true //probably not necessary
        }

        private inner class DrawStuff : JComponent() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)

                val graph2 = g as Graphics2D

                graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                list.forEach {
                    graph2.draw(it)
                }
            }
        }
    }

    fun render() {
        SwingUtilities.invokeLater {
            repaint()
        }
        this.list.clear()
    }
}