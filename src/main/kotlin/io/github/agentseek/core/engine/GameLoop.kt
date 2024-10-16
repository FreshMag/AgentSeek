package io.github.agentseek.core.engine

import kotlin.time.Duration

class GameLoop(startingPeriod: Duration) {
    var period = startingPeriod

    fun start(loopBehavior: (Duration) -> Unit) {

    }

    fun pause() {

    }

    fun stop() {

    }
}