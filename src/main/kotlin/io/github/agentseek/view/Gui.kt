package io.github.agentseek.view

import io.github.agentseek.core.engine.GameEngine
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.*
import kotlin.system.exitProcess


class Gui : JComponent() {
    companion object {
        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        const val APP_NAME = "Agent Seek"
        private const val START_BUTTON_TEXT = "Start Game"
        private const val QUIT_BUTTON_TEXT = "Quit"
    }

    fun startMainMenu() {
        val frame = JFrame("$APP_NAME main menu")
        val startButton = JButton(START_BUTTON_TEXT)
        val quitButton = JButton(QUIT_BUTTON_TEXT)
        val contentPane = JPanel(BorderLayout())
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        startButton.addActionListener {
            startGameGui()
            frame.isVisible = false
        }
        quitButton.addActionListener { exitProcess(0) }
        contentPane.add(startButton, BorderLayout.CENTER)
        contentPane.add(quitButton, BorderLayout.CENTER)
        frame.add(contentPane)
        frame.size = screenSize
        frame.isVisible = true
    }

    private fun startGameGui() {
        val frame = JFrame(APP_NAME)
        frame.size = screenSize
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
        GameEngine.start()
    }
}