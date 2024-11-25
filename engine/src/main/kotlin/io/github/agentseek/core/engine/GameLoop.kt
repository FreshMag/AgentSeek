package io.github.agentseek.core.engine

import kotlinx.coroutines.*
import java.lang.System.nanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

/**
 * Class that represents the game loop.
 *
 * @param startingPeriod The initial period of the game loop.
 * @param loopBehavior The behavior to execute in each loop iteration.
 */
class GameLoop(startingPeriod: Duration, private val loopBehavior: (Duration) -> Unit) {
    private var period = startingPeriod.toLong(DurationUnit.MILLISECONDS)

    private var isRunning = true
    private var isPaused = false
    private var job: Job? = null
    private var currentFrameTime: Long = 0L
    private var lastFrameTime: Long = 0L

    /**
     * Starts the game loop.
     *
     * @return The job representing the game loop coroutine.
     */
    fun start(): Job? {
        isRunning = true
        job = CoroutineScope(Dispatchers.Unconfined).launch { gameLoop() }
        return job
    }

    /**
     * Stops the game loop.
     */
    fun stop() {
        isRunning = false
        job?.cancel()
    }

    /**
     * Pauses the game loop.
     */
    fun pause() {
        isPaused = true
    }

    /**
     * Resumes the game loop.
     */
    fun resume() {
        isPaused = false
    }

    /**
     * Executes one iteration of the game loop with an artificial delta time.
     *
     * @param artificialDeltaTime The artificial delta time to use for this iteration.
     * @return The time taken to execute the loop iteration in milliseconds.
     */
    fun doOne(artificialDeltaTime: Duration): Long {
        val frameTime = measureTimeMillis {
            if (!isPaused) {
                loopBehavior(artificialDeltaTime)
            }
        }
        return frameTime
    }

    /**
     * The main game loop coroutine.
     */
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