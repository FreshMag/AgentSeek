package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.FieldMovementComponent
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.components.common.ComponentsUtils
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure
import java.awt.Color

class HearingAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    companion object {
        private const val ENEMY_NAME = "Player"
        private const val DEFAULT_RANDOM_TIMER = 3000L
        private const val DEFAULT_NOISE_TIMER = 5000L
    }

    private var randomTimer = TimerImpl(DEFAULT_RANDOM_TIMER)
    private var noiseTimer = TimerImpl(DEFAULT_NOISE_TIMER)
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private lateinit var fieldMovementComponent: FieldMovementComponent
    private var lastNoisePosition: Point2d? = null

    override fun init() {
        fieldMovementComponent = gameObject.getComponent<FieldMovementComponent>()!!
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction { perceptions ->
            val noisePosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.noisePosition
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
            percepts.add(Literal.parseLiteral("enemy_heard(${lastNoisePosition!!.x.toInt()}, ${lastNoisePosition!!.y.toInt()})"))
        } else if (noiseTimer.isElapsed()) {
            noiseTimer.reset()
        }
        checkPercepts()
        return percepts
    }

    private fun moveRandom() {
        val randomObjective = ComponentsUtils.getRandomVelocity(gameObject)
        if (!randomTimer.isStarted || randomTimer.isElapsed()) {
            randomTimer.restart()

        }
        synchronized(gameObject) {
            fieldMovementComponent.wakeUp()
            fieldMovementComponent.objective = randomObjective
        }
    }

    /**
     * Defines actions to take based on agent's perception
     */
    private fun checkPercepts() {
        if (lastNoisePosition != null && (randomTimer.isStarted && !randomTimer.isElapsed())) {
            noiseSensorComponent.noiseColor = Color.RED
        } else {
            noiseSensorComponent.noiseColor = Color.YELLOW
            lastNoisePosition = null
        }
    }

    /**
     * Set the objective to the coordinates [x] and [y]
     */
    private fun move(x: Int, y: Int) {
        synchronized(gameObject) {
            gameObject.rigidBody.velocity =
                Vector2d(x, y).minus(Vector2d(gameObject.position.x, gameObject.position.y)).normalized()
        }
    }
}