package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure
import java.awt.Color
import kotlin.math.sqrt

class GuardAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {

    companion object {
        private const val ENEMY_NAME = "Player"
        private val basePosition = Point2d(15, 0)
    }

    private lateinit var sightSensorComponent: SightSensorComponent
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private var lastEnemyPosition: Point2d? = null
    private var lastNoisePosition: Point2d? = null
    private val sightTimer = TimerImpl(5000)
    private val noiseTimer = TimerImpl(5000)
    override fun init() {
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
        sightSensorComponent.addReaction { perceptions ->
            val enemyPosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.enemyPosition
            if (enemyPosition != null) {
                lastEnemyPosition = enemyPosition
                sightTimer.restart()
            }
        }
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction { perceptions ->
            val noisePosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.noisePosition
            if (noisePosition != null) {
                lastEnemyPosition = noisePosition
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
        if (lastEnemyPosition != null) {
            percepts.add(Literal.parseLiteral("enemy_position(${lastEnemyPosition!!.x.toInt()}, ${lastEnemyPosition!!.y.toInt()})"))
        } else if (sightTimer.isElapsed()) {
            percepts.add(Literal.parseLiteral("enemy_lost"))
            percepts.add(Literal.parseLiteral("base_position(${basePosition.x}, ${basePosition.y})"))
        }

        if (isNearBase()) {
            percepts.add(Literal.parseLiteral("reached_base"))
        }
        checkPercepts()
        return percepts
    }

    private fun checkPercepts() {
        if (lastEnemyPosition != null && (sightTimer.isStarted && !sightTimer.isElapsed())) {
            sightSensorComponent.lightColor = Color.RED
        } else {
            sightSensorComponent.lightColor = Color.YELLOW
            lastEnemyPosition = null
        }
    }


    private fun isNearBase(): Boolean {
        val dx = gameObject.position.x - basePosition.x
        val dy = gameObject.position.y - basePosition.y
        return sqrt(dx * dx + dy * dy) <= 5.0
    }

    private fun move(x: Int, y: Int) {
        synchronized(gameObject) {
            gameObject.rigidBody.velocity =
                Vector2d(x, y).minus(Vector2d(gameObject.position.x, gameObject.position.y)).normalized()
        }
    }

    private fun moveRandom() {
        synchronized(gameObject) {
            gameObject.rigidBody.velocity = Vector2d(1.0, 1.0)
        }
    }
}
