package io.github.agentseek.util

import io.github.agentseek.core.engine.GameEngine
import kotlin.system.exitProcess

object GameREPL {
    private var isRunning = false
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
        while (true) {
            blockAndWaitLine()
        }
    }
}