package io.github.agentseek.util

import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory

object EngineTestingUtil {

    fun setUpEmptyScene(): Scene =
        SceneFactory.emptyScene().also { GameEngine.loadScene(it) }

}
