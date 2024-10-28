package io.github.agentseek.view.gui

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.input.Input
import io.github.agentseek.util.factories.Scenes
import io.github.agentseek.util.repl.GameREPL
import io.github.agentseek.view.*
import io.github.agentseek.view.input.InputListener
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE

typealias RenderingEvent = (Graphics2D) -> Unit

object GameGui : View, InputListener() {

    private var screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    override val camera: Camera = Camera(this, 50.0)

    private const val APP_NAME = "Agent Seek"

    private val renderingContext = RenderingContext<Graphics2D>(camera)
    private var eventsBuffer: List<RenderingEvent> = emptyList()
    private val frame = JFrame(APP_NAME)

    /**
     * This function is called once every frame rendering on the Graphics 2D of the GUI.
     */
    private val gameViewRendering: RenderingEvent = { g2d ->
        eventsBuffer.forEach {
            it(g2d)
        }
    }

    fun startGameGui(useRepl: Boolean = false) {
        frame.name = APP_NAME
        val panel = GameViewPanel(screenSize, gameViewRendering)
        frame.add(panel, BorderLayout.CENTER)
        frame.size = screenSize
        frame.preferredSize = screenSize
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.addKeyListener(this)
        panel.addMouseListener(this)
        Input.injectProvider(this)
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
            GameEngine.loadScene(Scenes.exampleScene(1))
            GameEngine.start()
        }
    }

    override fun render() {
        eventsBuffer = renderingContext.sinkToBuffer()
        SwingUtilities.invokeLater {
            frame.repaint()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getRenderingContext(): RenderingContext<T>? =
        renderingContext as? RenderingContext<T>

    override fun defaultRenderer(): Renderer<Graphics2D> = SimpleRenderer()
}
