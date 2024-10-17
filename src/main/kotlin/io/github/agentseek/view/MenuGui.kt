package io.github.agentseek.view

import java.awt.BorderLayout
import javax.swing.*
import kotlin.system.exitProcess


object MenuGui : JComponent() {

    private const val START_BUTTON_TEXT = "Start Game"
    private const val QUIT_BUTTON_TEXT = "Quit"


    fun startMainMenu() {
        val frame = JFrame("Main menu")
        val startButton = JButton(START_BUTTON_TEXT)
        val quitButton = JButton(QUIT_BUTTON_TEXT)
        val contentPane = JPanel(BorderLayout())
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        startButton.addActionListener {
            GameGui.startGameGui()
            frame.isVisible = false
        }
        quitButton.addActionListener { exitProcess(0) }
        contentPane.add(startButton, BorderLayout.CENTER)
        contentPane.add(quitButton, BorderLayout.CENTER)
        frame.add(contentPane)
        frame.pack()
        frame.isVisible = true
    }

}