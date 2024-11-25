package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.FieldMovementComponent
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.components.common.ComponentsUtils
import io.github.agentseek.components.common.ComponentsUtils.setRandomObjective
import io.github.agentseek.components.common.Config
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import io.github.agentseek.util.FastEntities.point
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure

/**
 * Component used by the hearing agent. The Hearing agent reacts to the noise made by the player, moving towards the
 * player's last known position.
 *
 * ### Percepts
 * The percepts returned by this agent are the following:
 * - *player_heard(X, Y)*
 *
 * ### Actions
 * The actions supported by this agent are the following:
 * - *moveRandom*: moves to a random position
 * - *moveToPosition(X, Y)*: moves to the position (X, Y)
 */
class HearingAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private var randomTimer = TimerImpl(Config.Agents.hearingRandomMovementTimerMillis)
    private var noiseTimer = TimerImpl(Config.Agents.hearingNoiseTimerMillis)
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private lateinit var fieldMovementComponent: FieldMovementComponent
    private var lastNoisePosition: Point2d? = null

    override fun init() {
        fieldMovementComponent = gameObject.getComponent<FieldMovementComponent>()!!
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction { perceptions ->
            val noisePosition = perceptions.find { it.gameObject.name == Config.Names.playerName }?.noisePosition
            if (noisePosition != null) {
                lastNoisePosition = noisePosition
                noiseTimer.restart()
            }
        }
    }

    override fun execute(action: Structure): Boolean {
        when (action.functor) {
            Actions.moveRandom.toString() -> {
                moveRandom()
            }

            Actions.moveToPosition.toString() -> {
                val (x, y) = (0..1).map {
                    ((action.getTerm(it) as NumberTerm).solve()).toInt()
                }
                move(x, y)
            }
        }
        return true
    }

    override fun getPercepts(): MutableList<Literal> {
        val percepts = mutableListOf<Literal>()
        if (lastNoisePosition != null) {
            percepts.add(Literal.parseLiteral("player_heard(${lastNoisePosition!!.x.toInt()}, ${lastNoisePosition!!.y.toInt()})"))
        } else if (noiseTimer.isElapsed()) {
            noiseTimer.reset()
        }
        checkPercepts()
        return percepts
    }

    /**
     * Moves the agent to a random position
     */
    private fun moveRandom() {
        this.setRandomObjective(randomTimer, Config.Agents.hearingMaxSpeed, fieldMovementComponent)
    }

    /**
     * Defines actions to take based on agent's perception
     */
    private fun checkPercepts() {
        if (lastNoisePosition != null && (randomTimer.isStarted && !randomTimer.isElapsed())) {
            noiseSensorComponent.noiseColor = Config.Agents.hearingDangerLightColor
        } else {
            noiseSensorComponent.noiseColor = Config.Agents.hearingStandardLightColor
            lastNoisePosition = null
        }
    }

    /**
     * Set the objective to the coordinates [x] and [y]
     */
    private fun move(x: Int, y: Int) {
        synchronized(gameObject) {
            fieldMovementComponent.maxVelocity = Config.Agents.hearingMaxSpeed
            fieldMovementComponent.wakeUp()
            fieldMovementComponent.objective = point(x, y)
        }
    }
}