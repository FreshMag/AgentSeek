package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.components.FieldMovementComponent
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.components.common.ComponentsUtils
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import io.github.agentseek.util.FastEntities.point
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure
import java.awt.Color

class GuardAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {

    companion object {
        private const val ENEMY_NAME = "Player"
        private val basePosition = Point2d(30, 30)
        private const val DEFAULT_NEAR_BASE_DISTANCE = 10.0
        private const val DEFAULT_RANDOM_TIMER = 4000L
        private const val DEFAULT_SIGHT_TIMER = 5000L
        private const val DEFAULT_NOISE_TIMER = 5000L
    }

    private lateinit var sightSensorComponent: SightSensorComponent
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private lateinit var fieldMovementComponent: FieldMovementComponent
    private var lastEnemyPosition: Point2d? = null
    private var lastNoisePosition: Point2d? = null
    private var randomTimer = TimerImpl(DEFAULT_RANDOM_TIMER)
    private val sightTimer = TimerImpl(DEFAULT_SIGHT_TIMER)
    private val noiseTimer = TimerImpl(DEFAULT_NOISE_TIMER)



    private val sightSensorReaction = { perceptions: List<SightSensorComponent.Perception> ->
        val enemyPosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.gameObject?.position
        if (enemyPosition != null) {
            lastEnemyPosition = enemyPosition
            sightTimer.restart()
        }
    }

    private val noiseSensorReaction = { perceptions: List<NoiseSensorComponent.Perception> ->
        val noisePosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.noisePosition
        if (noisePosition != null) {
            lastNoisePosition = noisePosition
            noiseTimer.restart()
        }
    }

    override fun init() {
        fieldMovementComponent = gameObject.getComponent<FieldMovementComponent>()!!
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
        sightSensorComponent.addReaction(sightSensorReaction)
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction(noiseSensorReaction)

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

            Actions.stop.toString() -> {
                fieldMovementComponent.stop()
            }

            Actions.checkSurroundings.toString() -> {
                sightSensorComponent.rotate(90)
            }
        }
        return true
    }

    override fun getPercepts(): MutableList<Literal> {
        val percepts = mutableListOf<Literal>()
        if (lastEnemyPosition != null) {
            percepts.add(Literal.parseLiteral("player_position(${lastEnemyPosition!!.x.toInt()}, ${lastEnemyPosition!!.y.toInt()})"))
        } else if (sightTimer.isElapsed()) {
            sightTimer.reset()
        }
        if (lastNoisePosition != null) {
            percepts.add(Literal.parseLiteral("enemy_heard(${lastNoisePosition!!.x.toInt()}, ${lastNoisePosition!!.y.toInt()})"))
        } else if (noiseTimer.isElapsed()) {
            noiseTimer.reset()
        }
        if (ComponentsUtils.isPointWithinDistance(
                firstWorldPoint = point(gameObject.position.x, gameObject.position.y), secondWorldPoint = point(
                    basePosition.x, basePosition.y
                ), maxDistance = DEFAULT_NEAR_BASE_DISTANCE
            )
        ) {
            percepts.add(Literal.parseLiteral("base_reached"))
        }
        checkPercepts()
        return percepts
    }

    /**
     * Defines actions to take based on agent's perception
     */
    private fun checkPercepts() {
        if (lastEnemyPosition != null && (sightTimer.isStarted && !sightTimer.isElapsed())) {
            sightSensorComponent.lightColor = Color.RED
        } else {
            sightSensorComponent.lightColor = Color.YELLOW
            lastEnemyPosition = null
        }
        if (lastNoisePosition != null && (noiseTimer.isStarted && !noiseTimer.isElapsed())) {
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
            fieldMovementComponent.wakeUp()
            fieldMovementComponent.objective = point(x, y)
        }
    }

    /**
     * Set the objective position to pseudo random coordinates
     */
    private fun moveRandom() {
        if (!randomTimer.isStarted || randomTimer.isElapsed()) {
            randomTimer.restart()
            var randomObjective: Point2d = ComponentsUtils.getRandomVelocity(gameObject)
            synchronized(gameObject) {
                fieldMovementComponent.wakeUp()
                fieldMovementComponent.objective = randomObjective
            }
        }
    }
}
