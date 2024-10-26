package io.github.agentseek.common

import kotlin.time.Duration
import kotlin.time.DurationUnit

class TimedAction(period: Duration, val action: () -> Unit) {

    private val timer: Timer = TimerImpl(period.toDouble(DurationUnit.MILLISECONDS)).also { it.startTimer() }

    fun applyIfElapsed() {
        if (timer.isElapsed()) {
            action()
            timer.reset()
        }
    }
}