package io.github.agentseek.view.gui

import io.github.agentseek.components.common.Config
import io.github.agentseek.core.Scene
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
    override val camera: Camera = Camera(this, Config.Camera.viewPortWidth)
    private val renderingContext = RenderingContext<Graphics2D>(camera)
    private var eventsBuffer: List<RenderingEvent> = emptyList()
    private val frame = JFrame(Config.GUI.frameTitle)

    /**
     * This function is called once every frame rendering on the Graphics 2D of the GUI.
     */
    private val gameViewRendering: RenderingEvent = { g2d ->
        eventsBuffer.forEach {
            it(g2d)
        }
    }

    /**
     * The following margin is used to avoid the frame's title bar. During runtime the frame's title bar is not counted
     * because the GameViewPanel is used instead, but before rendering the JFrame, panel dimensions are still 0, so this
     * manual adjustment is necessary.
     */
    private const val FRAME_HEIGHT_UPPER_MARGIN = 28

    private var screenSize: Dimension = Dimension(Config.GUI.frameWidth, Config.GUI.frameHeight)
    private val gameView: GameViewPanel = GameViewPanel(screenSize, gameViewRendering)
    override val screenHeight: Int
        get() = gameView.height.apply {
            return if (this == 0) {
                screenSize.height - FRAME_HEIGHT_UPPER_MARGIN
            } else {
                this
            }
        }
    override val screenWidth: Int
        get() = gameView.width.apply {
            return if (this == 0) {
                screenSize.width
            } else {
                this
            }
        }

    fun startGameGui(useRepl: Boolean = false, scene: Scene = Scenes.levelOne()) {
        frame.name = Config.GUI.frameTitle
        val panel = gameView
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
        start(useRepl, scene)
    }

    private fun start(useRepl: Boolean, scene: Scene) {
        if (useRepl) {
            Thread {
                GameREPL.start(scene)
            }.start()
        } else {
            GameEngine.loadScene(scene)
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
