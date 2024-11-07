package io.github.agentseek.common

import io.github.agentseek.core.engine.GameEngine
import kotlin.time.Duration
import kotlin.time.DurationUnit

class TimedAction(val id: String, period: Duration, val action: TimedAction.() -> Unit) {

    private val timer: Timer = TimerImpl(period.toLong(DurationUnit.MILLISECONDS)).also { it.startTimer() }

    fun applyIfElapsed() {
        if (timer.isElapsed()) {
            action()
            timer.reset()
        }
    }

    fun cancel() {
        GameEngine.cancel(id)
    }

}