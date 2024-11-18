package io.github.agentseek.events

import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.core.engine.GameEngine.log
import io.github.agentseek.util.serialization.Scenes.loadSceneFromResource

/**
 * This class manages a NewLevel Event and implements Event interface.
 */
class NewLevelEvent(val destinationSceneResourceName: String) : Event {

    override fun handle(state: Scene) {
        val newScene = loadSceneFromResource(destinationSceneResourceName)
        newScene?.let {
            GameEngine.loadScene(it)
        } ?: log("A NewLevelEvent was notified, but $destinationSceneResourceName was not found among resources.")
    }
}
