package io.github.agentseek.util.factories

import io.github.agentseek.core.Game
import io.github.agentseek.core.GameImpl
import io.github.agentseek.util.DummyComponent

object SceneFactory {
    fun replScene(): Pair<Game, DummyComponent> {
        val emptyScene = emptyScene()
        val dummyGO = emptyScene.world.gameObjectBuilder()
            .with { DummyComponent(it) }
            .build()

        emptyScene.world.addGameObject(dummyGO)
        return Pair(emptyScene, dummyGO.getComponent<DummyComponent>()!!)
    }

    fun emptyScene(): Game = GameImpl()

}