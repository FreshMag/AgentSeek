package io.github.agentseek.util

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory
import kotlin.system.exitProcess

object GameREPL {
    private var isRunning = false
    private var dummyComponent: DummyComponent? = null
    private fun parseLine(line: String) {
        when (line) {
            "start" -> {
                GameEngine.start()
                isRunning = true
            }
            "resume" -> {
                GameEngine.resume()
                isRunning = true
            }
            "exit" -> {
                GameEngine.stop()
                exitProcess(0)
            }
            else -> {
                if (isRunning) {
                    GameEngine.pause()
                    isRunning = false
                } else {
                    GameEngine.doOne()
                }
            }
        }
    }

    private fun blockAndWaitLine() {
        readlnOrNull()?.let { parseLine(it) }
    }


    fun start() {
        println("Welcome to the Game Engine REPL!")
        println("\t - Press ENTER to do one iteration of the loop or to stop an ongoing loop")
        println("\t - Digit 'start' to make the loop run indefinitely")
        println("\t - Digit 'resume' after having it paused to resume")
        println("\t - Digit 'exit' to exit the application")
        val (replScene, dummyComponent) = SceneFactory.replScene()
        this.dummyComponent = dummyComponent
        GameEngine.loadScene(replScene)
        while (true) {
            blockAndWaitLine()
        }
    }
}