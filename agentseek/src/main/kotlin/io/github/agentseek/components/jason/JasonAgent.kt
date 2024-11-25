package io.github.agentseek.components.jason

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import jason.asSyntax.Literal
import jason.asSyntax.Structure

/**
 * Abstract class for Jason agents.
 */
abstract class JasonAgent(gameObject: GameObject) : AbstractComponent(gameObject) {
    /**
     * ID for this Agent, corresponding to a Jason agent ID
     */
    abstract val id: String

    /**
     * Handler for the actions performed by this agent.
     */
    abstract fun execute(action: Structure): Boolean

    /**
     * Gets the percepts for this Agent
     */
    abstract fun getPercepts(): MutableList<Literal>
}