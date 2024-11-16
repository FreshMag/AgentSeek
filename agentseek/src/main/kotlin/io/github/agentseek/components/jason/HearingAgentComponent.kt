package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure
import java.awt.Color
import kotlin.random.Random

class HearingAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private var randomTimer = TimerImpl(3000)
    private var noiseTimer = TimerImpl(5000)
    private var velocityX = 0
    private var velocityY = 0
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private var lastNoisePosition: Point2d? = null

    override fun init() {
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction { perceptions ->
            val noisePosition = perceptions.find { it.gameObject.name == "Player" }?.noisePosition
            if (noisePosition != null) {
                lastNoisePosition = noisePosition
                noiseTimer.restart()
            }
        }
    }

    override fun execute(action: Structure) {
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
        if (!randomTimer.isStarted || randomTimer.isElapsed()) {
            randomTimer.restart()
            setRandomVelocity()
        }
        synchronized(gameObject) {
            gameObject.rigidBody.velocity = Vector2d(velocityX, velocityY)
        }
    }

    private fun checkPercepts() {
        if (lastNoisePosition != null && (randomTimer.isStarted && !randomTimer.isElapsed())) {
            noiseSensorComponent.noiseColor = Color.RED
        } else {
            noiseSensorComponent.noiseColor = Color.YELLOW
            lastNoisePosition = null
        }
    }

    private fun setRandomVelocity() {
        do {
            velocityX = Random.nextInt(-1, 2)
            velocityY = Random.nextInt(-1, 2)
        } while (velocityX == 0 && velocityY == 0)
    }

    private fun move(x: Int, y: Int) {
        synchronized(gameObject) {
            gameObject.rigidBody.velocity =
                Vector2d(x, y).minus(Vector2d(gameObject.position.x, gameObject.position.y)).normalized()
        }
    }
}