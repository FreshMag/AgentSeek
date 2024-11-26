package io.github.agentseek.util.repl

import io.github.agentseek.components.Component
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.DummyComponent
import io.github.agentseek.util.factories.SceneFactory
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine

/**
 * REPL for the game engine.
 */
object GameREPL {
    /**
     * The scene being used in the REPL.
     */
    lateinit var scene: Scene
    private lateinit var dummyComponent: DummyComponent
    /**
     * Whether the game is running.
     */
    var isRunning = false
    private val replCommand = REPLParsing.REPLCommand()
    private val cmd = CommandLine(replCommand)
    private val terminal = TerminalBuilder.terminal()
    private val reader = LineReaderBuilder.builder()
        .terminal(terminal)
        .build()

    /**
     * Parses a line of input.
     */
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

    /**
     * Starts the REPL. If a scene is provided, it will be used, otherwise a default scene will be created.
     *
     * *Note*: this function is blocking and will not return.
     */
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
        this.scene.gameObjects.forEach { it.components.forEach(Component::init) }
        while (true) {
            val input = reader.readLine("> ")
            parseLine(input)
        }
    }

}

/**
 * Main function for the REPL.
 */
fun main() {
    GameREPL.start()
}
