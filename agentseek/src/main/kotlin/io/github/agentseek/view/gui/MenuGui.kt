package io.github.agentseek.view.gui

import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import javax.swing.*
import kotlin.system.exitProcess


object MenuGui {

    private const val APPLICATION_NAME = "Agent Seek"
    private const val START_BUTTON_TEXT = "Start Game"
    private const val START_WITH_REPL_BUTTON_TEXT = "Start Game with REPL"
    private const val QUIT_BUTTON_TEXT = "Quit"
    private lateinit var frame: JFrame

    fun startMainMenu() {
        frame = JFrame(APPLICATION_NAME)
        frame.add(makePanelWithButtons())
        frame.pack()
        frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        frame.isVisible = true
    }

    private fun makePanelWithButtons(): JPanel {
        val contentPane = JPanel()
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)

        val titleLabel = JLabel(APPLICATION_NAME)
        titleLabel.font = Font("Arial", Font.BOLD, 32)
        titleLabel.alignmentX = Component.CENTER_ALIGNMENT

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

        quitButton.addActionListener { exitProcess(0) }

        val verticalSpacing = 20

        contentPane.add(Box.createRigidArea(Dimension(0, 50)))
        contentPane.add(titleLabel)
        contentPane.add(Box.createRigidArea(Dimension(0, verticalSpacing)))
        contentPane.add(startButton)
        contentPane.add(Box.createRigidArea(Dimension(0, verticalSpacing)))
        contentPane.add(startWithReplButton)
        contentPane.add(Box.createRigidArea(Dimension(0, verticalSpacing)))
        contentPane.add(quitButton)
        contentPane.add(Box.createRigidArea(Dimension(0, verticalSpacing)))

        contentPane.preferredSize = Dimension(400, 300)

        return contentPane
    }

}