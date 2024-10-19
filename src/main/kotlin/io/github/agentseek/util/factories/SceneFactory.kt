package io.github.agentseek.util.factories

import io.github.agentseek.core.Game
import io.github.agentseek.core.GameImpl
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.DummyComponent
import io.github.agentseek.view.SimpleRenderer

object SceneFactory {
    fun replScene(): Pair<Game, DummyComponent> {
        val emptyScene = emptyScene()
        val dummyGO = emptyScene.world.gameObjectBuilder()
            .with { DummyComponent(it) }
            .renderer(SimpleRenderer())
            .build()
        val colliderGO = emptyScene.world.gameObjectBuilder()
            .position(10.0, 5.0)
            .rigidBody { RigidBody.CircleRigidBody(3.0, it) }
            .renderer(SimpleRenderer())
            .build()

        emptyScene.world.addGameObject(dummyGO)
        emptyScene.world.addGameObject(colliderGO)
        return Pair(emptyScene, dummyGO.getComponent<DummyComponent>()!!)
    }

    fun emptyScene(): Game = GameImpl()

}