package io.github.agentseek.common

/**
 * This class is an implementation of Timer interface and is used to measure a time range.
 * @param waitTimeMillis Time to wait.
 */
class TimerImpl(private var waitTimeMillis: Long) : Timer {
    private var startMills: Long = 0

    /**
     * Checks if the timer is already working.
     */
    val isStarted: Boolean
        get() = startMills != 0L

    /**
     * Constructor method to instantiate a Timer given the amount of time to wait.
     */
    init {
        reset()
    }

    override fun isElapsed(): Boolean {
        return isStarted && System.currentTimeMillis() - startMills > waitTimeMillis
    }

    override fun startTimer() {
        startMills = System.currentTimeMillis()
    }

    override fun setWaitTime(wait: Long) {
        waitTimeMillis = wait
    }

    override fun reset() {
        startMills = 0
    }

    override fun restart() {
        startMills = System.currentTimeMillis()
    }
}
