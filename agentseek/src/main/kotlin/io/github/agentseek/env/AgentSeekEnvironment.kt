package io.github.agentseek.env

import io.github.agentseek.view.gui.MenuGui
import jason.environment.Environment

class AgentSeekEnvironment : Environment() {

    override fun init(args: Array<out String>?) {
        super.init(args)
        MenuGui.startMainMenu()
    }
}