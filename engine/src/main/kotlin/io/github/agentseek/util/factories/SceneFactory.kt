package io.github.agentseek.util.factories

import io.github.agentseek.core.Scene
import io.github.agentseek.core.SceneImpl
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.DummyComponent

object SceneFactory {
    fun replScene(): Pair<Scene, DummyComponent> {
        val emptyScene = emptyScene()
        val defaultRenderer = GameEngine.view?.defaultRenderer() ?: throw IllegalStateException("Missing view")
        val dummyGO = emptyScene.world.gameObjectBuilder()
            .with { DummyComponent(it) }
            .renderer(defaultRenderer)
            .build()
        val colliderGO = emptyScene.world.gameObjectBuilder()
            .position(10.0, 5.0)
            .rigidBody { RigidBody.CircleRigidBody(it, 3.0) }
            .renderer(defaultRenderer)
            .build()

        emptyScene.world.addGameObject(dummyGO)
        emptyScene.world.addGameObject(colliderGO)
        return Pair(emptyScene, dummyGO.getComponent<DummyComponent>()!!)
    }

    fun emptyScene(): Scene = SceneImpl()

}