package io.github.agentseek.util.jason

import io.github.agentseek.util.jason.Utils.numberToTerm
import jason.asSemantics.DefaultInternalAction
import jason.asSemantics.TransitionSystem
import jason.asSemantics.Unifier
import jason.asSyntax.Term

class get_random_available_angle : DefaultInternalAction(){
    override fun execute(ts: TransitionSystem?, un: Unifier, args: Array<out Term>): Any {
        println("Getting available angles for turning")
        val available = listOf(90, -90)
        val angle = available.random()
        return un.unifies(args[0], numberToTerm(angle))
    }
}
