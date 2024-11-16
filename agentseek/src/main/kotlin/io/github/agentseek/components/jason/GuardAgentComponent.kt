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
import kotlin.random.Random

class GuardAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {

    companion object {
        private const val ENEMY_NAME = "Player"
        private val basePosition = Point2d(25, 0)
        private const val DEFAULT_NEAR_BASE_DISTANCE = 5.0
        private const val DEFAULT_RANDOM_TIMER = 4000L
        private const val DEFAULT_SIGHT_TIMER = 5000L
        private const val DEFAULT_NOISE_TIMER = 5000L
    }

    private lateinit var sightSensorComponent: SightSensorComponent
    private lateinit var noiseSensorComponent: NoiseSensorComponent
    private var lastEnemyPosition: Point2d? = null
    private var lastNoisePosition: Point2d? = null
    private var randomTimer = TimerImpl(DEFAULT_RANDOM_TIMER)
    private val sightTimer = TimerImpl(DEFAULT_SIGHT_TIMER)
    private val noiseTimer = TimerImpl(DEFAULT_NOISE_TIMER)
    private var velocityX = 0
    private var velocityY = 0


    private val sightSensorReaction = { perceptions: List<SightSensorComponent.Perception> ->
        val enemyPosition = perceptions.find { it.gameObject.name == ENEMY_NAME }?.enemyPosition
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
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
        sightSensorComponent.addReaction(sightSensorReaction)
        noiseSensorComponent = gameObject.getComponent<NoiseSensorComponent>()!!
        noiseSensorComponent.addReaction(noiseSensorReaction)
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
            sightTimer.reset()
            percepts.add(Literal.parseLiteral("enemy_lost"))
            percepts.add(Literal.parseLiteral("base_position(${basePosition.x}, ${basePosition.y})"))
        }
        if (isNearBase()) {
            percepts.add(Literal.parseLiteral("base_reached"))
        }
        if (lastNoisePosition != null) {
            percepts.add(Literal.parseLiteral("enemy_heard(${lastNoisePosition!!.x.toInt()}, ${lastNoisePosition!!.y.toInt()})"))
        } else if (noiseTimer.isElapsed()) {
            noiseTimer.reset()
        }
        checkPercepts()
        return percepts
    }

    /*
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
     * Determines if the game object is near the base.
     *
     * This method calculates the Euclidean distance between the game object's current position
     * and a predefined base position. If the distance is less than or equal to the
     * DEFAULT_NEAR_BASE_DISTANCE, it returns true indicating the object is near the base.
     *
     * @return true if the game object is near the base, false otherwise.
     */
    private fun isNearBase(): Boolean {
        val dx = gameObject.position.x - basePosition.x
        val dy = gameObject.position.y - basePosition.y
        return sqrt(dx * dx + dy * dy) <= DEFAULT_NEAR_BASE_DISTANCE
    }


    /**
     * Moves the game object to a specified position.
     *
     * This method takes target coordinates (x, y) and computes the velocity vector needed to move
     * the game object from its current position to the target position. The computed velocity
     * vector is then normalized and assigned to the game object's rigid body within a synchronized block.
     *
     * @param x The target x-coordinate to move the game object to.
     * @param y The target y-coordinate to move the game object to.
     */
    private fun move(x: Int, y: Int) {
        synchronized(gameObject) {
            gameObject.rigidBody.velocity =
                Vector2d(x, y).minus(Vector2d(gameObject.position.x, gameObject.position.y)).normalized()
        }
    }

    /**
     * Moves the game object in a random direction.
     *
     * This method is called periodically to set a random velocity to the game object's rigid body
     * within a synchronized block.
     *
     * The random direction is determined by the `setRandomVelocity` method, which assigns random
     * values to `velocityX` and `velocityY`.
     *
     * The movement direction is updated only if the `randomTimer` is not started or its time has elapsed.
     * In those cases, the timer is restarted and a new random velocity is set.
     */
    private fun moveRandom() {
        if (!randomTimer.isStarted || randomTimer.isElapsed()) {
            randomTimer.restart()
            setRandomVelocity()
        }
        synchronized(gameObject) {
            gameObject.rigidBody.velocity = Vector2d(velocityX, velocityY)
        }
    }

    /**
     * Sets a random velocity for the game object.
     *
     * This method assigns random integers to `velocityX` and `velocityY`, ranging from -1 to 1.
     * The loop ensures that at least one of the velocities is non-zero, preventing the object
     * from remaining stationary.
     */
    private fun setRandomVelocity() {
        do {
            velocityX = Random.nextInt(-1, 2)
            velocityY = Random.nextInt(-1, 2)
        } while (velocityX == 0 && velocityY == 0)
    }
}
