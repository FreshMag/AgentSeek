package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.TimerImpl
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.env.Actions
import jason.asSyntax.Literal
import jason.asSyntax.NumberTerm
import jason.asSyntax.Structure

class GuardAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {
    private lateinit var sightSensorComponent: SightSensorComponent
    private var lastEnemyPosition: Point2d? = null
    private val timer = TimerImpl(5000)
    override fun init() {
        sightSensorComponent = gameObject.getComponent<SightSensorComponent>()!!
        sightSensorComponent.addReaction {
            lastEnemyPosition = it.find { it.gameObject.name == "Player" }?.enemyPosition
            if (lastEnemyPosition != null) {
                println("START")
                timer.startTimer()
            }
        }
    }

    override fun execute(action: Structure) {
        when (action.functor) {
            Actions.moveRandom.toString() -> {
                println("move random")
                moveRandom()
            }

            Actions.moveToPosition.toString() -> {
                val (x, y) = (0..1).map {
                    ((action.getTerm(it) as NumberTerm).solve()).toInt()
                }
                println("Attempting to move to coordinates ($x, $y)")
                move(x, y)
            }
        }
    }

    override fun getPercepts(): MutableList<Literal> {
        val percepts = mutableListOf<Literal>()
        lastEnemyPosition?.let {
            println("EnemySeen at ${it.x.toInt()},${it.y.toInt()}")
            percepts.add(Literal.parseLiteral("enemy_position(${it.x.toInt()}, ${it.y.toInt()})"))
        }
        if (timer.isElapsed()) {
            percepts.add(Literal.parseLiteral("enemy_lost"))
            percepts.add(Literal.parseLiteral("base_position(${50}, ${0})"))
        }
        return percepts
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
