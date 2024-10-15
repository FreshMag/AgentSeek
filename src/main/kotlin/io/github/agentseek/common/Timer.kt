package io.github.agentseek.common

/**
 * This interface represents a Timer used to measure a time range.
 */
interface Timer {
    /**
     * Checks if the timer is elapsed.
     *
     * @return True if the time is elapsed, false otherwise.
     */
    fun isElapsed(): Boolean

    /**
     * Starts a new timer.
     */
    fun startTimer()

    /**
     * Sets a new Wait time.
     *
     * @param wait Time, in milliseconds, to wait.
     */
    fun setWaitTime(wait: Double)
}
