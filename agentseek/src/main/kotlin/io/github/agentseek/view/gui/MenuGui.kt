package io.github.agentseek.view.gui

import io.github.agentseek.util.serialization.Scenes.loadSceneFromResource
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import java.io.File
import javax.swing.*
import javax.swing.border.EmptyBorder
import kotlin.system.exitProcess



object MenuGui {

    private var screenSize: Dimension = Dimension(720, 720)//Toolkit.getDefaultToolkit().screenSize
    private const val APPLICATION_NAME = "Agent Seek"
    private const val START_BUTTON_TEXT = "Start Game"
    private const val START_SELECTED_BUTTON_TEXT = "Start selected"
    private const val START_WITH_REPL_BUTTON_TEXT = "Start Game with REPL"
    private const val START_EDITOR_TEXT = "Start Editor"
    private const val START_EDITOR_EMPTY_TEXT = "Start Editor with empty scene"
    private const val QUIT_BUTTON_TEXT = "Quit"

    private var selectedPath: String = ""

    private val editorButton = JButton(START_EDITOR_TEXT).apply {
        preferredSize = Dimension(200, 50)
        maximumSize = preferredSize
        alignmentX = Component.CENTER_ALIGNMENT
        isEnabled = false
    }

    private val startSelected = JButton(START_SELECTED_BUTTON_TEXT).apply {
        preferredSize = Dimension(200, 50)
        maximumSize = preferredSize
        alignmentX = Component.CENTER_ALIGNMENT
        isEnabled = false
    }

    private lateinit var frame: JFrame

    fun startMainMenu() {
        frame = JFrame(APPLICATION_NAME)
        frame.size = screenSize
        val container = JPanel().apply { layout = BorderLayout() }
        container.add(makeTitlePanel(), BorderLayout.NORTH)
        container.add(makePanelWithButtons(), BorderLayout.CENTER)
        container.add(makeScenesPanel(), BorderLayout.WEST)
        frame.add(container, BorderLayout.CENTER)
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.isVisible = true
    }

    private fun makeTitlePanel(): JPanel {
        val container = JPanel()
        val titleLabel = JLabel(APPLICATION_NAME)
        titleLabel.font = Font("Arial", Font.BOLD, 32)
        titleLabel.alignmentX = Component.CENTER_ALIGNMENT

        container.add(Box.createRigidArea(Dimension(0, 50)))
        container.add(titleLabel)
        return container
    }

    private fun makePanelWithButtons(): JPanel {
        val contentPane = JPanel()
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)

        val verticalSpacing = 20

        fun addButton(button: JButton) {
            contentPane.add(Box.createRigidArea(Dimension(0, verticalSpacing)))
            contentPane.add(button)
        }

        val startButton = JButton(START_BUTTON_TEXT).apply {
            preferredSize = Dimension(200, 50)
            maximumSize = preferredSize
            alignmentX = Component.CENTER_ALIGNMENT
        }

        val startWithReplButton = JButton(START_WITH_REPL_BUTTON_TEXT).apply {
            preferredSize = Dimension(200, 50)
            maximumSize = preferredSize
            alignmentX = Component.CENTER_ALIGNMENT
        }


        val quitButton = JButton(QUIT_BUTTON_TEXT).apply {
            preferredSize = Dimension(200, 50)
            maximumSize = preferredSize
            alignmentX = Component.CENTER_ALIGNMENT
        }

        startButton.addActionListener {
            GameGui.startGameGui()
            frame.isVisible = false
        }

        startWithReplButton.addActionListener {
            frame.isVisible = false
            GameGui.startGameGui(true)
        }

        startSelected.addActionListener {
            frame.isVisible = false
            val scene = this.loadSceneFromResource(selectedPath)!!
            GameGui.startGameGui(false, scene = scene)
        }

        editorButton.addActionListener {
            frame.isVisible = false
            val scene = this.loadSceneFromResource(selectedPath)!!
            EditorGui.start(scene = scene, selectedPath)
        }

        val emptyEditorButton = JButton(START_EDITOR_EMPTY_TEXT).apply {
            preferredSize = Dimension(200, 50)
            maximumSize = preferredSize
            alignmentX = Component.CENTER_ALIGNMENT
        }.also {
            it.addActionListener {
                frame.isVisible = false
                EditorGui.start()
            }
        }

        quitButton.addActionListener { exitProcess(0) }

        listOf(startButton, startWithReplButton, startSelected, editorButton, emptyEditorButton, quitButton).forEach {
            addButton(it)
        }

        contentPane.preferredSize = Dimension(720, 720)

        return contentPane
    }

    private fun makeScenesPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        val fileListModel = DefaultListModel<String>()
        val fileList = JList(fileListModel)
        fileList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        fileList.addListSelectionListener {
            if (!fileList.isSelectionEmpty) {
                val selectedFileName = fileList.selectedValue
                onFileSelected(selectedFileName)
            }
        }

        val scrollPane = JScrollPane(fileList)
        scrollPane.preferredSize = Dimension(200, 50)
        scrollPane.border = EmptyBorder(5, 5, 5, 5)

        panel.add(JLabel("Scenes:").also { it.border = EmptyBorder(5, 0, 5, 5) }, BorderLayout.NORTH)
        panel.add(scrollPane, BorderLayout.CENTER)
        panel.border = EmptyBorder(5, 20, 50, 5)
        refreshFileList(fileListModel)

        return panel
    }

    private fun getScenesBasePath(): String {
        val resourceUrl = MenuGui::class.java.getResource("/yaml/scenes")
        return resourceUrl?.path ?: throw IllegalStateException("Scenes directory not found.")
    }

    private fun refreshFileList(fileListModel: DefaultListModel<String>) {
        fileListModel.clear()
        val basePath = getScenesBasePath()
        val directory = File(basePath)
        if (directory.exists() && directory.isDirectory) {
            directory.listFiles()?.forEach { file ->
                if (file.isFile && file.name.endsWith(".yaml")) {
                    val relativeFileName = file.name.substringBefore(".")
                    fileListModel.addElement(relativeFileName)
                }
            }
        }
    }

    private fun onFileSelected(filePath: String) {
        // println("File selected: $filePath")
        editorButton.isEnabled = true
        startSelected.isEnabled = true
        selectedPath = filePath
    }
}