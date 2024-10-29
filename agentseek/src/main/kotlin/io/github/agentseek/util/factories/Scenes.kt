package io.github.agentseek.util.factories

import io.github.agentseek.core.Scene
import io.github.agentseek.util.serialization.Scenes.loadSceneFromResource

object Scenes {

    fun exampleScene(): Scene {
        return loadSceneFromResource("SimpleCollision")!!
    }

    fun mapBordersScene(): Scene {
        return loadSceneFromResource("SimpleMapBorders")!!
    }
}