package io.github.agentseek.components.jason

import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.util.FastEntities.radians
import io.github.agentseek.util.GameObjectUtilities.center
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import io.github.agentseek.util.jason.Utils.termToInteger
import jason.asSyntax.Literal
import jason.asSyntax.Structure

class CameraAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private var seesPlayer = false
    private lateinit var bounds: List<GameObject>

    override fun init() {
        sightSensorComponent = SightSensorComponent(gameObject, 10.0, radians(30))
        gameObject.addComponent(sightSensorComponent)
        sightSensorComponent.init()
        sightSensorComponent.addReaction {
            seesPlayer = it.any { it.gameObject.name == "Player" }
        }
        bounds = gameObject.otherGameObjects().filter { it.name.lowercase() in listOf("bound", "obstacle", "wall") }
    }

    override fun execute(action: Structure): Boolean {
        when (action.functor) {
            "rotate" -> {
                val rotation = termToInteger(action.terms[0])
                sightSensorComponent.rotate(rotation)
            }
        }
        return true
    }

    private fun hasWallLeftRight(): Pair<Boolean, Boolean> {
        val direction =  sightSensorComponent.directionOfSight
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

    private data class Percepts(val seesPlayerPercept: String?, val wallLeft: Boolean, val wallRight: Boolean) {
        fun toLiterals(): MutableList<Literal> = mutableListOf<Literal>().also { list ->
            seesPlayerPercept?.let {
                list.add(Literal.parseLiteral(it))
            }
            if (wallLeft) list.add(Literal.parseLiteral("wallLeft"))
            if (wallRight) list.add(Literal.parseLiteral("wallRight"))
        }
    }

    override fun getPercepts(): MutableList<Literal> =
        synchronized(this) {
            val walls = hasWallLeftRight()
            Percepts(if (seesPlayer) "seesPlayer" else null, walls.first, walls.second)
        }.toLiterals()

    companion object {
        /**
         * How close can be an "obstacle", "bound" or "wall" to be detected by the camera agent (and so excluding
         * that direction when rotating)
         */
        const val WALL_AWARENESS = 2.0
    }
}