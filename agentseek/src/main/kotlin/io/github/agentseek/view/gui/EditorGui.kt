package io.github.agentseek.view.gui

import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.util.repl.GameREPL
import io.github.agentseek.view.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.time.Duration.Companion.seconds

object EditorGui : View {
    private var screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    private const val APP_NAME = "Agent Seek - Editor"

    override val camera: Camera = Camera(this, 50.0)
    private val renderingContext = RenderingContext<Graphics2D>(GameGui.camera)
    private var eventsBuffer: List<RenderingEvent> = emptyList()
    private val frame = JFrame(APP_NAME)
    private var selectedPath: String = "./"
    private lateinit var scene: Scene

    private val gameViewRendering: RenderingEvent = { g2d ->
        eventsBuffer.forEach {
            it(g2d)
        }
    }

    fun start(scene: Scene? = null, selectedPath: String? = null) {
        if (scene != null && selectedPath != null) {
            this.scene = scene
            this.selectedPath = selectedPath
        } else {
            this.scene = emptyScene()
        }
        frame.name = APP_NAME
        val panel = GameViewPanel(screenSize, gameViewRendering)
        frame.add(panel, BorderLayout.CENTER)
        frame.size = screenSize
        frame.preferredSize = screenSize
        frame.defaultCloseOperation = EXIT_ON_CLOSE
        frame.isVisible = true

        frame.addComponentListener(object : ComponentAdapter() {
            override fun componentResized(e: ComponentEvent?) {
                screenSize = e?.component?.size ?: screenSize
            }
        })
        GameEngine.view = this
        Thread {
            GameREPL.start(this.scene)
        }.start()
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