package io.github.agentseek.util.repl

import io.github.agentseek.core.Game
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.DummyComponent
import io.github.agentseek.util.factories.SceneFactory
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import picocli.CommandLine


object GameREPL {
    lateinit var scene: Game
    private lateinit var dummyComponent: DummyComponent

    var isRunning = false
    private val reader: LineReader = LineReaderBuilder.builder().build()

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
        val replCommand = REPLParsing.REPLCommand()
        val cmd = CommandLine(replCommand)
        try {
            cmd.execute(*line.split(" ").toTypedArray())
        } catch (ex: Exception) {
            println("Error: ${ex.message}")
        }
    }

    fun start() {
        println("Welcome to the Game Engine REPL!")
        println("Type help to show usage")
        val (replScene, dummyComponent) = SceneFactory.replScene()
        GameREPL.dummyComponent = dummyComponent
        scene = replScene
        GameEngine.loadScene(replScene)
        while (true) {
            val input = reader.readLine("")
            parseLine(input)
        }
    }

}

fun main() {
    GameREPL.start()
}
