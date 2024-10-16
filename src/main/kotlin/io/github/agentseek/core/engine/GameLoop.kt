package io.github.agentseek.core.engine

import kotlinx.coroutines.*
import java.lang.System.nanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

class GameLoop(startingPeriod: Duration, private val loopBehavior: (Duration) -> Unit) {
    private var period = startingPeriod.toLong(DurationUnit.MILLISECONDS)

    private var isRunning = true
    private var isPaused = false
    private var job: Job? = null

    fun start(): Job? {
        isRunning = true
        job = CoroutineScope(Dispatchers.Unconfined).launch { gameLoop() }
        return job
    }

    fun stop() {
        isRunning = false
        job?.cancel()
    }

    fun pause() {
        isPaused = true
    }

    fun resume() {
        isPaused = false
    }

    private suspend fun gameLoop() {
        var lastFrameTime = nanoTime()
        while (isRunning) {
            val currentFrameTime = nanoTime()
            val deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000.0 // Convert nanoseconds to milliseconds
            lastFrameTime = currentFrameTime
            val frameTime = measureTimeMillis {
                if (!isPaused) {
                    loopBehavior(deltaTime.milliseconds)
                }
            }
            delay((period - frameTime).coerceAtLeast(0L))
        }
    }
}