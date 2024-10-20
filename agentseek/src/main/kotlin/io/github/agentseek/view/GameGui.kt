package io.github.agentseek.view

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory
import io.github.agentseek.util.repl.GameREPL
import java.awt.*
import java.util.*
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE


object GameGui : View {
    val screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    private const val APP_NAME = "Agent Seek"

    private val shapesToDraw: MutableList<Shape> = Collections.synchronizedList(mutableListOf())
    private var buffer: List<Shape> = emptyList()
    private val frame = JFrame(APP_NAME)

    fun startGameGui(repl: Boolean = false) {
        frame.name = APP_NAME
        frame.add(TestingPanelGraphics(), BorderLayout.CENTER)
        frame.size = screenSize
        frame.preferredSize = screenSize
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isVisible = true
        GameEngine.view = this
        if (repl) {
            Thread {
                GameREPL.start()
            }.start()
        } else {
            GameEngine.loadScene(SceneFactory.replScene().first)
            GameEngine.start()
        }
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

                buffer.forEach {
                    graph2.draw(it)
                }
            }
        }
    }

    override fun render() {
        buffer = shapesToDraw.toList()
        shapesToDraw.clear()
        SwingUtilities.invokeLater {
            frame.repaint()
        }

    }

    override fun draw(obj: Any) {
        when(obj) {
            is Shape -> shapesToDraw.add(obj)
            else -> println("Unknown object to draw: $obj")
        }
    }
}