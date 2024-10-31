package io.github.agentseek.env

import io.github.agentseek.view.gui.MenuGui
import jason.asSyntax.Structure
import jason.environment.Environment

class AgentSeekEnvironment : Environment() {

    override fun init(args: Array<out String>?) {
        super.init(args)
    }

    override fun executeAction(agName: String?, act: Structure?): Boolean {
        println("$agName is doing something")
        return true
    }
}