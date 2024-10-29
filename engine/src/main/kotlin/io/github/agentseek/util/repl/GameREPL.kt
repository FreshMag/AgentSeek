package io.github.agentseek.util.repl

import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.DummyComponent
import io.github.agentseek.util.factories.SceneFactory
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine


object GameREPL {
    lateinit var scene: Scene
    private lateinit var dummyComponent: DummyComponent

    var isRunning = false
    private val replCommand = REPLParsing.REPLCommand()
    private val cmd = CommandLine(replCommand)
    private val terminal = TerminalBuilder.terminal()
    private val reader = LineReaderBuilder.builder()
        .terminal(terminal)
        .build()

    private fun parseLine(line: String) {
        if (line.isEmpty()) {
            if (isRunning) {
                GameEngine.pause()
                isRunning = false
            } else {
                GameEngine.doOne()
            }
            return
        }

        try {
            cmd.execute(*line.split(" ").toTypedArray())
        } catch (ex: Exception) {
            println("Error: ${ex.message}")
        }
    }

    fun start(scene: Scene? = null) {
        println("Welcome to the Game Engine REPL!")
        println("\t - Press ENTER to do one iteration of the game loop")
        println("\t - Or type help to show usage")
        if (scene == null) {
            val (replScene, dummyComponent) = SceneFactory.replScene()
            GameREPL.dummyComponent = dummyComponent
            this.scene = replScene
        } else {
            this.scene = scene
        }
        GameEngine.loadScene(this.scene)
        while (true) {
            val input = reader.readLine("> ")
            parseLine(input)
        }
    }

}

fun main() {
    GameREPL.start()
}
