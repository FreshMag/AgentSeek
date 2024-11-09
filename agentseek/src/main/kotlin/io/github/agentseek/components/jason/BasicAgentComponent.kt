package io.github.agentseek.components.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.core.GameObject
import jason.asSyntax.Literal
import jason.asSyntax.Structure

class BasicAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {

    override fun execute(action: Structure): Boolean {
        println("Inside Engine: Agent: $id, Executing action: $action")
        return true
    }

    override fun getPercepts(): MutableList<Literal> {
        var position: Point2d
        synchronized(gameObject) {
            position = gameObject.position
        }
        return mutableListOf(
            Literal.parseLiteral("position(${position.x}, ${position.y})")
        )
    }
}