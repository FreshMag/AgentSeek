package io.github.agentseek.env

import io.github.agentseek.components.jason.JasonAgent
import io.github.agentseek.components.jason.JasonInitializerComponent
import io.github.agentseek.env.Actions.linkAction
import io.github.agentseek.util.GameObjectUtilities.otherGameObjects
import jason.asSyntax.Literal
import jason.asSyntax.Structure
import jason.environment.Environment
import java.util.*

class AgentSeekEnvironment : Environment() {

    override fun init(args: Array<out String>?) {
        super.init(args)
    }

    private val agents: MutableMap<String, JasonAgent> = Collections.synchronizedMap(mutableMapOf())

    private fun link(id: String) {
        val manager = JasonInitializerComponent.jasonManager
            ?: throw IllegalStateException("Jason initializer inside the engine is not initialized")
        val go = manager.otherGameObjects().firstOrNull { it.getComponent<JasonAgent>()?.id == id }
        if (go != null) {
            agents[id] = go.getComponent<JasonAgent>()!!
        } else {
            throw IllegalStateException("Agent with $id is not present in the scene!")
        }
    }

    override fun executeAction(agName: String, action: Structure): Boolean {
        agents[agName]?.execute(action)
        when (action) {
            linkAction -> {
                link(agName)
            }
        }
        return true
    }

    override fun getPercepts(agName: String?): MutableCollection<Literal> {
        clearPercepts(agName)
        return agents[agName]?.getPercepts() ?: mutableListOf()
    }

}