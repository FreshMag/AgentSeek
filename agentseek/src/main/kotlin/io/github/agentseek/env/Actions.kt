package io.github.agentseek.env

import jason.asSyntax.Literal

object Actions {

    /**
     * Linking action for a Jason agent. It links with the engine action.
     */
    val linkAction: Literal = Literal.parseLiteral("link(this)")
}