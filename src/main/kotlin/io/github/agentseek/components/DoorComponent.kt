package io.github.agentseek.components

import io.github.agentseek.common.Timer
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.NewLevelEvent
import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * Component used to describe door's behavior.
 */
class DoorComponent(gameObject: GameObject, duration: Int) : AbstractComponent(gameObject) {
    /**
     * Checks if the door is collidable.
     */
    private var isCollidable: Boolean = false
    private val timer: Timer = TimerImpl(duration.toDouble())

    override fun init() {
        timer.startTimer()
    }

    override fun onUpdate(deltaTime: Duration) {
        if (timer.isElapsed() || isCollidable) {
            this.isCollidable = true
            if (player?.hitBox?.isCollidingWith(gameObject.hitBox) == true) {
                notifyEvent(NewLevelEvent())
            }
        }
    }
}
