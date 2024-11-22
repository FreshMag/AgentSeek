package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.FastEntities.allDirections
import io.github.agentseek.util.FastEntities.radians
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import io.github.agentseek.util.jason.Utils.toLiteral
import io.github.agentseek.util.jason.Utils.toNumber
import jason.asSyntax.Literal
import jason.asSyntax.Structure
import java.awt.Color
import kotlin.time.Duration

/**
 * Component used by the camera agent. The Camera agent checks the surroundings, looking for the player.
 * When it sees it, it starts tracking it, returning appropriate percepts.
 *
 * ### Percepts
 * The percepts returned by this agent are the following:
 * - *seesPlayer*
 * - *wallLeft*
 * - *wallRight*
 * - *playerPosition(X, Y)*
 *
 * ### Actions
 * The actions supported by this agent are the following:
 * - *rotate/1*: rotates the camera by *X* degrees
 */
class CameraAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private var seesPlayer = false
    private lateinit var bounds: List<GameObject>
    private var player: GameObject? = null
    private lateinit var startingDirection: Vector2d

    /**
     * Reaction to the sight of the player.
     */
    private val sightReaction = { perceptions: List<SightSensorComponent. Perception> ->
        seesPlayer = perceptions.any {
            (it.gameObject.name == PLAYER_GO_NAME).also { isPlayer -> if (isPlayer) player = it.gameObject }
        }
        if (seesPlayer) {
            sightSensorComponent.lightColor = DANGER_LIGHT_COLOR
        } else {
            sightSensorComponent.setDirection(startingDirection)
            sightSensorComponent.lightColor = STANDARD_LIGHT_COLOR
        }
    }

    override fun init() {
        bounds = gameObject.otherGameObjects().filter { namesToExclude.contains(it.name.lowercase()) }
        // Choose a direction that doesn't intersect with a bound as starting direction
        startingDirection = allDirections().first { direction ->
            bounds.none { it.rigidBody.shape.contains(gameObject.center() + (direction * WALL_AWARENESS)) }
        }
        sightSensorComponent = SightSensorComponent(
            gameObject,
            SIGHT_LENGTH,
            radians(SIGHT_APERTURE_DEGREES),
            namesToExclude
        )
        sightSensorComponent.setDirection(startingDirection)
        gameObject.addComponent(sightSensorComponent)
        sightSensorComponent.init()
        sightSensorComponent.addReaction(sightReaction)
    }

    override fun execute(action: Structure): Boolean {
        when (action.functor) {
            "rotate" -> {
                val rotation = action.terms[0].toNumber()
                synchronized(this) {
                    sightSensorComponent.rotate(rotation)
                }
            }
        }
        return true
    }

    override fun onUpdate(deltaTime: Duration) {
        if (seesPlayer) {
            player?.let {
                sightSensorComponent.setDirection(it.center() - gameObject.center())
            }
        }
    }

    /**
     * Returns a pair where the first value is `true` if the camera has a wall on the left, while the second if it
     * has a wall on the right.
     */
    private fun hasWallLeftRight(): Pair<Boolean, Boolean> {
        val direction = sightSensorComponent.directionOfSight
        val leftVector = direction.leftNormal() * WALL_AWARENESS
        val rightVector = direction.rightNormal() * WALL_AWARENESS
        val center = gameObject.center()
        return bounds.fold(Pair(false, false)) { acc, go ->
            Pair(
                acc.first || go.rigidBody.shape.contains(center + leftVector),
                acc.second || go.rigidBody.shape.contains(center + rightVector)
            )
        }
    }

    /**
     * Utility class used to track the perceptions of the camera agent.
     */
    private data class Percepts(
        val seesPlayerPerception: String?,
        val wallLeft: Boolean,
        val wallRight: Boolean,
        val playerPosition: Point2d
    ) {
        fun toLiterals(): MutableList<Literal> = mutableListOf<Literal>().also { list ->
            seesPlayerPerception?.let {
                list.add(Literal.parseLiteral(it))
            }
            if (wallLeft) list.add(Literal.parseLiteral(WALL_LEFT_BELIEF))
            if (wallRight) list.add(Literal.parseLiteral(WALL_RIGHT_BELIEF))
            list.add(playerPosition.toLiteral(PLAYER_POSITION_BELIEF))
        }
    }

    override fun getPercepts(): MutableList<Literal> =
        synchronized(this) {
            val walls = hasWallLeftRight()
            Percepts(
                if (seesPlayer) SEES_PLAYER_BELIEF else null,
                walls.first,
                walls.second,
                player?.position ?: Point2d.origin()
            )
        }.toLiterals()

    companion object {

        /**
         * Belief used by the agent to check if it sees the player.
         */
        private const val SEES_PLAYER_BELIEF = "seesPlayer"

        /**
         * Belief used by the agent to check if it has a wall on the left.
         */
        private const val WALL_LEFT_BELIEF = "wallLeft"

        /**
         * Belief used by the agent to check if it has a wall on the right.
         */
        private const val WALL_RIGHT_BELIEF = "wallRight"

        /**
         * Belief used by the agent to maintain the player position.
         */
        private const val PLAYER_POSITION_BELIEF = "playerPosition"

        /**
         * How close can be an "obstacle", "bound" or "wall" to be detected by the camera agent (and so excluding
         * that direction when rotating).
         */
        private const val WALL_AWARENESS = 2.0

        /**
         * Length of the cone of sight.
         */
        private const val SIGHT_LENGTH = 10.0

        /**
         * Aperture of the cone of sight (in degrees).
         */
        private const val SIGHT_APERTURE_DEGREES = 30

        /**
         * Names used for the wall awareness.
         */
        private val namesToExclude = setOf("bound", "obstacle", "wall", "bounds")

        /**
         * Name used to identify the player's GameObject.
         */
        private const val PLAYER_GO_NAME = "Player"

        /**
         * Color used when the camera is in normal state.
         */
        private val STANDARD_LIGHT_COLOR = Color.YELLOW

        /**
         * Color used when the camera detects the player.
         */
        private val DANGER_LIGHT_COLOR = Color.RED
    }
}