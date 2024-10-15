package io.github.agentseek.components

import io.github.agentseek.common.Timer
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.controller.events.NewLevelEvent
import io.github.agentseek.core.GameObject
import io.github.agentseek.world.World

/**
 * Component used to describe door's behavior.
 */
class DoorComponent(world: World, duration: Int) : AbstractComponent(world) {
    /**
     * Checks if the door is collidable.
     */
    private var isCollidable: Boolean = false
        private set
    private val timer: Timer = TimerImpl(duration.toDouble())

    override fun onAdded(gameObject: GameObject) {
        super.onAdded(gameObject)
        timer.startTimer()
    }

    override fun onUpdate(deltaTime: Double) {
        if (timer.isElapsed() || isCollidable) {
            this.isCollidable = true
            if (player?.hitBox?.isCollidingWith(gameObject.hitBox) == true) {
                world.notifyWorldEvent(NewLevelEvent())
            }
        }
    }
}