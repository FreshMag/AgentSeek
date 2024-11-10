package io.github.agentseek.util.jason

import io.github.agentseek.util.jason.Utils.toTerm
import jason.asSemantics.DefaultInternalAction
import jason.asSemantics.TransitionSystem
import jason.asSemantics.Unifier
import jason.asSyntax.Literal
import jason.asSyntax.Term

class get_random_available_angle : DefaultInternalAction() {
    override fun execute(ts: TransitionSystem, un: Unifier, args: Array<out Term>): Any {
        println("Getting available angles for turning")
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
