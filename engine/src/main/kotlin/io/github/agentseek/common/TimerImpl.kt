package io.github.agentseek.common

/**
 * This class is an implementation of Timer interface and is used to measure a time range.
 * @param waitTimeMillis Time to wait.
 */
class TimerImpl(waitTimeMillis: Double) : Timer {
    private var startMills = 0.0
    private var elapsedMills: Double

    /**
     * Checks if the timer is already working.
     */
    val isStarted: Boolean
        get() = startMills != 0.0

    /**
     * Constructor method to instantiate a Timer given the amount of time to wait.
     */
    init {
        reset()
        elapsedMills = waitTimeMillis
    }

    override fun isElapsed(): Boolean {
        if (System.currentTimeMillis() - startMills > elapsedMills) {
            reset()
            return true
        }
        return false
    }

    override fun startTimer() {
        startMills = System.currentTimeMillis().toDouble()
    }

    override fun setWaitTime(wait: Double) {
        elapsedMills = wait
    }

    /**
     * Resets the Timer.
     */
    override fun reset() {
        startMills = 0.0
    }
}
