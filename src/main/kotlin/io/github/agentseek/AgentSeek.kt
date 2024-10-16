package io.github.agentseek

import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.GameEngine.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.system.exitProcess

fun blockAndWaitLine() {
    val str = readlnOrNull()
    if (str == "exit") {
        GameEngine.stop()
        exitProcess(0)
    }
}

suspend fun main() {
    log("Press ENTER to start the loop, or digit 'exit' to close the program")
    withContext(Dispatchers.IO) {
        blockAndWaitLine()
        log("Starting the loop. Press ENTER to pause, or digit 'exit' to close the program")
        GameEngine.start()
        while (true) {
            blockAndWaitLine()
            GameEngine.pause()
            log("Pausing the loop. Press ENTER to resume, or digit 'exit' to close the program")
            blockAndWaitLine()
            GameEngine.resume()
        }
    }
}