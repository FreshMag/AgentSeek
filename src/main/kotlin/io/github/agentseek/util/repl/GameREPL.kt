package io.github.agentseek.util.repl

import io.github.agentseek.core.Game
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.DummyComponent
import io.github.agentseek.util.factories.SceneFactory
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine


object GameREPL {
    lateinit var scene: Game
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

    fun start() {
        println("Welcome to the Game Engine REPL!")
        println("\t - Press ENTER to do one iteration of the game loop")
        println("\t - Or type help to show usage")
        val (replScene, dummyComponent) = SceneFactory.replScene()
        GameREPL.dummyComponent = dummyComponent
        scene = replScene
        GameEngine.loadScene(replScene)
        while (true) {
            val input = reader.readLine("> ")
            parseLine(input)
        }
    }

}

fun main() {
    GameREPL.start()
}
