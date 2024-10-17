package io.github.agentseek.util

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.components.Component
import io.github.agentseek.core.Game
import io.github.agentseek.core.GameObject
import kotlin.time.Duration

object FastComponent {

    fun Game.createComponent(initFun: Component.() -> Unit = {},
                             updateFun: Component.(Duration) -> Unit = {},
                             onRemovedFun: Component.() -> Unit = {},
                             addToGameObject: Boolean = true,
                             addToWorld: Boolean = true
    ): Pair<GameObject, Component> {
        val go = world.gameObjectBuilder().build()
        val component = object : AbstractComponent(go) {
            override fun init() { initFun() }
            override fun onUpdate(deltaTime: Duration) { updateFun(deltaTime) }
            override fun onRemoved() { onRemovedFun() }
        }
        if (addToGameObject) {
            go.addComponent(component)
        }
        if (addToWorld) {
            world.addGameObject(go)
        }
        return Pair(go, component)
    }
}