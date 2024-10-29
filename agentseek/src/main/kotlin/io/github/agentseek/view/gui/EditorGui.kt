package io.github.agentseek.view.gui

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.util.repl.GameREPL
import io.github.agentseek.util.serialization.save
import io.github.agentseek.view.*
import io.github.agentseek.view.Renderer
import io.github.agentseek.view.editor.Utilities.addClickListener
import io.github.agentseek.view.editor.Utilities.addGameObjectDialog
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.swing.*
import javax.swing.WindowConstants.EXIT_ON_CLOSE
import kotlin.system.exitProcess

object EditorGui : View {
    private var screenSize: Dimension = Dimension(1000, 720)//Toolkit.getDefaultToolkit().screenSize
    override val screenHeight: Int
        get() = screenSize.height
    override val screenWidth: Int
        get() = screenSize.width

    private const val APP_NAME = "Agent Seek - Editor"

    override val camera: Camera = Camera(this, 50.0)
    private val renderingContext = RenderingContext<Graphics2D>(camera)
    private var eventsBuffer: List<RenderingEvent> = emptyList()
    private val frame = JFrame(APP_NAME)
    private var selectedPath: String? = null
    private lateinit var scene: Scene
    var selectedGo: GameObject? = null

    private val gameViewRendering: RenderingEvent = { g2d ->
        eventsBuffer.forEach {
            it(g2d)
        }
    }
    private var panel: JPanel = GameViewPanel(screenSize, gameViewRendering)

    fun start(scene: Scene? = null, selectedPath: String? = null) {
        if (scene != null && selectedPath != null) {
            this.scene = scene
            this.selectedPath = selectedPath
        } else {
            this.scene = emptyScene()
        }
        frame.name = APP_NAME
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

        addJMenu()

        panel.addClickListener(scene!!)

        GameEngine.view = this
        Thread {
            GameREPL.start(this.scene)
        }.start()
    }

    private fun addJMenu() {
        val menuBar = JMenuBar()
        val fileMenu = JMenu("File")
        val saveItem = JMenuItem("Save")
        val exitItem = JMenuItem("Exit")

        saveItem.addActionListener {
            val fileChooser = JFileChooser(File("./"))
            fileChooser.dialogTitle = "Save File"
            fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
            val result = fileChooser.showSaveDialog(frame)

            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFile = fileChooser.selectedFile
                scene.save(selectedFile.parent, selectedFile.nameWithoutExtension)
            }
        }

        exitItem.addActionListener {
            exitProcess(0)
        }

        fileMenu.add(saveItem)
        fileMenu.addSeparator()
        fileMenu.add(exitItem)

        val editMenu = JMenu("Edit")
        val addItem = JMenuItem("Add GameObject")
        val moveItem = JMenuItem("Move center")

        addItem.addActionListener {
            addGameObjectDialog(frame, scene)
        }
        moveItem.addActionListener {
            val previous = panel.mouseListeners.first()
            panel.removeMouseListener(previous)
            panel.addMouseListener(object : MouseListener {
                override fun mouseClicked(e: MouseEvent?) {
                    val x = e?.x ?: return
                    val y = e.y
                    GameEngine.view?.camera?.toWorldPoint(Point2d(x.toDouble(), y.toDouble()))?.let{
                        println("Moved ${selectedGo?.id} to (${it.x}, ${it.y})")
                        selectedGo?.position = it
                        panel.removeMouseListener(this)
                        panel.addMouseListener(previous)
                    }
                }
                override fun mousePressed(e: MouseEvent?) {}
                override fun mouseReleased(e: MouseEvent?) {}
                override fun mouseEntered(e: MouseEvent?) {}
                override fun mouseExited(e: MouseEvent?) {}

            })
        }

        editMenu.add(addItem)
        editMenu.add(moveItem)

        val engineMenu = JMenu("Engine")
        val doOneItem = JMenuItem("Do 1")

        doOneItem.addActionListener {
            GameEngine.doOne()
        }

        engineMenu.add(doOneItem)

        menuBar.add(fileMenu)
        menuBar.add(editMenu)
        menuBar.add(engineMenu)

        frame.jMenuBar = menuBar
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