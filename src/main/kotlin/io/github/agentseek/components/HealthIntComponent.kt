package io.github.agentseek.components

import io.github.agentseek.common.Timer
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.core.GameObject
import io.github.agentseek.events.GameOverEvent
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration

/**
 * This component is used to track an object's health points. Here, hps are implemented as an integer.
 */
class HealthIntComponent(
    gameObject: GameObject,
    /**
     * Sets max amount of health points.
     */
    private var maxHp: Int
) : AbstractComponent(gameObject) {
    /**
     * Current amount of health points left.
     */
    private var currentHp: Int = maxHp

    /**
     * Whether this component is currently immortal or not.
     * @return True if this component is currently immortal.
     */
    private var isImmortal: Boolean = false
    private val timer: Timer = TimerImpl(WAIT_TIME)

    override fun onUpdate(deltaTime: Duration) {
        if (timer.isElapsed()) {
            this.isImmortal = false
        }
        if (this.currentHp <= 0) {
            if (gameObject.isPlayer()) {
                notifyEvent(GameOverEvent())
            } else {
                gameObject.delete()
            }
        }
    }

    /**
     * Decreases [currentHp] of a quantity equals to [dmg]. If it's below zero, it will call a death event.
     */
    fun damage(dmg: Int) {
        if (gameObject.isPlayer()) {
            if (!this.isImmortal) {
                this.currentHp -= dmg
                this.setImmortality()
            }
        } else {
            this.currentHp -= dmg
        }
    }

    /**
     * Increases [currentHp] of quantity [heal] only if not exceeds [maxHp].
     */
    fun heal(heal: Int) {
        this.currentHp = min(this.maxHp, this.currentHp + heal)
    }

    fun maxOutHp() {
        this.currentHp = maxHp
    }

    /**
     * Increases max amount of health points by [inc].
     */
    fun increaseMaxHp(inc: Int) {
        this.maxHp += inc
    }

    /**
     * Decreases max health by [dec]. Can't have maximum health below 1. If current HP is
     * bigger than new maxHp, currentHp is automatically set to maximum.
     */
    fun decreaseMaxHp(dec: Int) {
        this.maxHp = max(1, this.maxHp - dec)
        this.currentHp = min(currentHp, maxHp)
    }

    /**
     * Sets Immortality on and starts timer: immortality will be set to false after timer is elapsed.
     */
    private fun setImmortality() {
        timer.startTimer()
        this.isImmortal = true
    }

    companion object {
        private const val WAIT_TIME = 2000.0
    }
}
