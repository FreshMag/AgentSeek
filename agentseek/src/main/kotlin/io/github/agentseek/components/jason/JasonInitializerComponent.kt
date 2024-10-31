package io.github.agentseek.components.jason

import io.github.agentseek.AgentSeek
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.core.GameObject
import jason.infra.local.RunLocalMAS

class JasonInitializerComponent(gameObject: GameObject, private val mas2jPath: String) : AbstractComponent(gameObject) {

    override fun init() {
        val file = AgentSeek::class.java.getResource(mas2jPath)?.path
        Thread {
            RunLocalMAS.main(arrayOf(file))
        }.start()
    }
}