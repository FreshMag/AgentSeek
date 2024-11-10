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

class CameraAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private var seesPlayer = false
    private lateinit var bounds: List<GameObject>
    private var player: GameObject? = null
    private var namesToExclude = setOf("bound", "obstacle", "wall", "bounds")
    private lateinit var startingDirection: Vector2d

    override fun init() {
        bounds = gameObject.otherGameObjects().filter { namesToExclude.contains(it.name.lowercase()) }
        startingDirection = allDirections().first { direction ->
            bounds.none { it.rigidBody.shape.contains(gameObject.center() + (direction * WALL_AWARENESS)) }
        }
        sightSensorComponent = SightSensorComponent(
            gameObject,
            10.0,
            radians(30),
            namesToExclude
        )
        sightSensorComponent.setDirection(startingDirection)
        gameObject.addComponent(sightSensorComponent)
        sightSensorComponent.init()
        sightSensorComponent.addReaction {
            seesPlayer = it.any {
                (it.gameObject.name == "Player").also { isPlayer -> if (isPlayer) player = it.gameObject }
            }
            if (seesPlayer) {
                sightSensorComponent.lightColor = Color.RED
            } else {
                sightSensorComponent.setDirection(startingDirection)
                sightSensorComponent.lightColor = Color.YELLOW
            }
        }

    }

    override fun execute(action: Structure): Boolean {
        when (action.functor) {
            "rotate" -> {
                val rotation = action.terms[0].toNumber()
                sightSensorComponent.rotate(rotation)
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

    private fun hasWallLeftRight(): Pair<Boolean, Boolean> {
        val direction = sightSensorComponent.directionOfSight
        val leftVector = direction.rotateDegrees(90.0) * WALL_AWARENESS
        val rightVector = direction.rotateDegrees(-90.0) * WALL_AWARENESS
        val center = gameObject.center()
        return bounds.fold(Pair(false, false)) { acc, go ->
            Pair(
                acc.first || go.rigidBody.shape.contains(center + leftVector),
                acc.second || go.rigidBody.shape.contains(center + rightVector)
            )
        }
    }

    private data class Percepts(
        val seesPlayerPercept: String?,
        val wallLeft: Boolean,
        val wallRight: Boolean,
        val playerPosition: Point2d
    ) {
        fun toLiterals(): MutableList<Literal> = mutableListOf<Literal>().also { list ->
            seesPlayerPercept?.let {
                list.add(Literal.parseLiteral(it))
            }
            if (wallLeft) list.add(Literal.parseLiteral("wallLeft"))
            if (wallRight) list.add(Literal.parseLiteral("wallRight"))
            list.add(playerPosition.toLiteral("playerPosition"))
        }
    }

    override fun getPercepts(): MutableList<Literal> =
        synchronized(this) {
            val walls = hasWallLeftRight()
            Percepts(
                if (seesPlayer) "seesPlayer" else null,
                walls.first,
                walls.second,
                player?.position ?: Point2d.origin()
            )
        }.toLiterals()

    companion object {
        /**
         * How close can be an "obstacle", "bound" or "wall" to be detected by the camera agent (and so excluding
         * that direction when rotating)
         */
        const val WALL_AWARENESS = 2.0
    }
}