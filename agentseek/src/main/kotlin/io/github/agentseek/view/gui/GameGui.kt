package io.github.agentseek.view.gui

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory
import io.github.agentseek.util.repl.GameREPL
import io.github.agentseek.view.GameViewPanel
import io.github.agentseek.view.View
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.*
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE


object GameGui : View {
    private var screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    private const val APP_NAME = "Agent Seek"

    private val shapesToDraw: MutableList<Shape> = Collections.synchronizedList(mutableListOf())
    private var shapeBuffer: List<Shape> = emptyList()
    private val frame = JFrame(APP_NAME)

    private val gameViewRendering: (Graphics2D) -> Unit = { g2d ->
        shapeBuffer.forEach {
            g2d.draw(it)
        }
    }

    fun startGameGui(useRepl: Boolean = false) {
        frame.name = APP_NAME
        frame.add(GameViewPanel(screenSize, gameViewRendering), BorderLayout.CENTER)
        frame.size = screenSize
        frame.preferredSize = screenSize
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isVisible = true

        // Makes this GUI responsive
        frame.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                screenSize = e?.component?.size ?: screenSize
            }
        })
        // Sets GameEngine view
        GameEngine.view = this
        // Starts the game view
        start(useRepl)
    }

    private fun start(useRepl: Boolean) {
        if (useRepl) {
            Thread {
                GameREPL.start()
            }.start()
        } else {
            GameEngine.loadScene(SceneFactory.replScene().first)
            GameEngine.start()
        }
    }

    override fun render() {
        shapeBuffer = shapesToDraw.toList()
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
