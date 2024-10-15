package io.github.agentseek.common

/**
 * This class is an implementation of Timer interface and is used to measure a time range.
 * @param waitTime Time to wait.
 */
class TimerImpl(waitTime: Double) : Timer {
    private var startMills = 0.0
    private var elapsedMills: Double

    /**
     * Checks if the timer is already working.
     */
    private val isStarted: Boolean
        get() = startMills != 0.0

    /**
     * Constructor method to instantiate a Timer given the amount of time to wait.
     */
    init {
        reset()
        elapsedMills = waitTime
    }

    override fun isElapsed(): Boolean {
        if (isStarted && System.currentTimeMillis() - startMills > elapsedMills) {
            reset()
            return true
        }
        return false
    }

    override fun startTimer() {
        if (!isStarted) {
            startMills = System.currentTimeMillis().toDouble()
        }
    }

    override fun setWaitTime(wait: Double) {
        elapsedMills = wait
    }

    /**
     * Resets the Timer.
     */
    private fun reset() {
        startMills = 0.0
    }
}
