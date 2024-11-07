package io.github.agentseek.common

/**
 * This interface represents a Timer used to measure a time range in milliseconds.
 */
interface Timer {
    /**
     * Checks if the timer is elapsed.
     */
    fun isElapsed(): Boolean

    /**
     * Starts a new timer.
     */
    fun startTimer()

    /**
     * Sets a new [wait] time in milliseconds.
     */
    fun setWaitTime(wait: Long)

    /**
     * Resets the timer.
     */
    fun reset()

    /**
     * Restart the timer
     */
    fun restart()
}
