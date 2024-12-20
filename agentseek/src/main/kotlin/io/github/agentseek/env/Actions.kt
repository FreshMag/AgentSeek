package io.github.agentseek.env

import jason.asSyntax.Literal

/**
 * Object that contains the actions that the agents can perform.
 */
object Actions {

    /**
     * Linking action for a Jason agent. It links with the engine action.
     */
    val linkAction: Literal = Literal.parseLiteral("link(this)")

    /**
     * Action to simulate a random walk.
     */
    val moveRandom: Literal = Literal.parseLiteral("moveRandom")

    /**
     * Action to make the agent follow an enemy.
     */
    val moveToPosition: Literal = Literal.parseLiteral("move")

    /**
     * Action to make the agent defend the base
     */
    val stop: Literal = Literal.parseLiteral("stop")

    /**
     * Action to make the agent rotate to check the surroundings
     */
    val checkSurroundings: Literal = Literal.parseLiteral("checkSurroundings")
}