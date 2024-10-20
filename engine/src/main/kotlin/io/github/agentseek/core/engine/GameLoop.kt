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
    private var currentFrameTime: Long = 0L
    private var lastFrameTime: Long = 0L

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

    fun doOne(artificialDeltaTime: Duration): Long {
        val frameTime = measureTimeMillis {
            if (!isPaused) {
                loopBehavior(artificialDeltaTime)
            }
        }
        return frameTime
    }

    private suspend fun gameLoop() {
        lastFrameTime = nanoTime()
        while (isRunning) {
            currentFrameTime = nanoTime()
            val deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000.0 // Convert nanoseconds to milliseconds
            val frameTime = doOne(deltaTime.milliseconds)
            lastFrameTime = currentFrameTime
            delay((period - frameTime).coerceAtLeast(0L))
        }
    }
}