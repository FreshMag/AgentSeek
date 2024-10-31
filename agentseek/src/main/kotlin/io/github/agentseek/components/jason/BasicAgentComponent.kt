package io.github.agentseek.components.jason

import io.github.agentseek.core.GameObject

class BasicAgentComponent(gameObject: GameObject, override val id: String) : JasonAgent(gameObject) {

    override fun execute(action: String) {
        println("Inside Engine: Agent: $id, Executing action: $action")
    }
}