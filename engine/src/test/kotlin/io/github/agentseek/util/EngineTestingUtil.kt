package io.github.agentseek.util

import io.github.agentseek.core.Game
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory

object EngineTestingUtil {

    fun setUpEmptyScene(): Game =
        SceneFactory.emptyScene().also { GameEngine.loadScene(it) }

}
