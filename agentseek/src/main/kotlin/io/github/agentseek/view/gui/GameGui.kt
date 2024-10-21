package io.github.agentseek.view.gui

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.Scenes
import io.github.agentseek.util.repl.GameREPL
import io.github.agentseek.view.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.util.*
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.random.Random


object GameGui : View {

    data class RenderingEvent(val behavior: (Graphics2D) -> Unit)

    private var screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    override val camera: Camera = Camera(this, 50.0)

    private const val APP_NAME = "Agent Seek"

    private val renderingEvents: MutableList<RenderingEvent> = Collections.synchronizedList(mutableListOf())
    private var eventsBuffer: List<RenderingEvent> = emptyList()
    private val frame = JFrame(APP_NAME)

    /**
     * This function is called once every frame rendering on the Graphics 2D of the GUI.
     */
    private val gameViewRendering: (Graphics2D) -> Unit = { g2d ->
        eventsBuffer.forEach {
            it.behavior(g2d)
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
            GameEngine.loadScene(Scenes.exampleScene(Random.nextInt(10)))
            GameEngine.start()
        }
    }

    override fun render() {
        eventsBuffer = renderingEvents.toList()
        renderingEvents.clear()
        SwingUtilities.invokeLater {
            frame.repaint()
        }
    }

    override fun draw(obj: Any) {
        when (obj) {
            is RenderingEvent -> renderingEvents.add(obj)
            else -> println("Unknown object to draw: $obj")
        }
    }

    override fun defaultRenderer(): Renderer = SimpleRenderer()
}
