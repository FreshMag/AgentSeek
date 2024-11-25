package io.github.agentseek.common

import io.github.agentseek.core.engine.GameEngine
import kotlin.time.Duration
import kotlin.time.DurationUnit

/**
 * A timed action that is executed periodically.
 * @param id the unique identifier of the timed action
 * @param period the period of the timed action
 * @param action the action to be executed
 */
class TimedAction(val id: String, period: Duration, val action: TimedAction.() -> Unit) {

    private val timer: Timer = TimerImpl(period.toLong(DurationUnit.MILLISECONDS)).also { it.startTimer() }

    /**
     * Applies the action if the timer has elapsed and resets the timer, otherwise does nothing.
     */
    fun applyIfElapsed() {
        if (timer.isElapsed()) {
            action()
            timer.reset()
        }
    }

    /**
     * Cancels the timed action.
     */
    fun cancel() {
        GameEngine.cancel(id)
    }

}