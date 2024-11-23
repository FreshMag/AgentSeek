package io.github.agentseek.util

import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory
import kotlin.time.Duration

object EngineTestingUtil {

    fun setUpEmptyScene(): Scene =
        SceneFactory.emptyScene().also { GameEngine.loadScene(it) }

    /**
     * Creates a component from a scene, returning the created GameObject and Component
     */
    fun Scene.createComponent(
        initFun: Component.() -> Unit = {},
        updateFun: Component.(Duration) -> Unit = {},
        onRemovedFun: Component.() -> Unit = {},
        addToGameObject: Boolean = true,
        addToWorld: Boolean = true
    ): Pair<GameObject, Component> {
        val go = world.gameObjectBuilder().build().also {
            if (addToWorld) {
                world.addGameObject(it)
            }
        }
        val component = object : AbstractComponent(go) {
            override fun init() {
                initFun()
            }

            override fun onUpdate(deltaTime: Duration) {
                updateFun(deltaTime)
            }

            override fun onRemoved() {
                onRemovedFun()
            }
        }.also {
            if (addToGameObject) {
                go.addComponent(it)
            }
        }

        return Pair(go, component)
    }

}
