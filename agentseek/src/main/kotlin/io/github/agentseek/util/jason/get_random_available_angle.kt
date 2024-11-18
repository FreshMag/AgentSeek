package io.github.agentseek.util.jason

import io.github.agentseek.util.jason.Utils.toTerm
import jason.asSemantics.DefaultInternalAction
import jason.asSemantics.TransitionSystem
import jason.asSemantics.Unifier
import jason.asSyntax.Literal
import jason.asSyntax.Term

/**
 * Internal action used by the camera agent to obtain a random available angle for rotation. If it has a wall on one
 * side (left or right), checked by the beliefs `wallLeft` and `wallRight`, it returns a subset of `{90, -90}`.
 */
class get_random_available_angle : DefaultInternalAction() {
    override fun execute(ts: TransitionSystem, un: Unifier, args: Array<out Term>): Any {
        val currentAgent = ts.ag
        val wallLeft = currentAgent.believes(Literal.parseLiteral("wallLeft"), un)
        val wallRight = currentAgent.believes(Literal.parseLiteral("wallRight"), un)

        val available = mutableListOf<Int>()
        if (!wallLeft) available.add(90)
        if (!wallRight) available.add(-90)

        if (available.isEmpty()) {
            println("Cannot move anywhere!"); return false
        }
        val angle = available.random()
        return un.unifies(args[0], angle.toTerm())
    }
}
