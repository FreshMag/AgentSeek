package io.github.agentseek.util.factories

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.ConstantAccelerationComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.view.swing.SimpleRenderer
import kotlin.random.Random

object Scenes {

    fun exampleScene(nObjects: Int): Scene {
        val emptyScene = emptyScene()
        val maxX = GameEngine.view?.camera?.viewPortWidth ?: 50.0
        val maxY = GameEngine.view?.camera?.viewPortHeight ?: 50.0
        (0 until nObjects).forEach { _ ->
            val go = emptyScene.world.gameObjectBuilder()
                .position(Random.nextDouble(maxX), Random.nextDouble(maxY))
                .with {
                    ConstantAccelerationComponent(
                        it,
                        Vector2d(Random.nextDouble(3.0), Random.nextDouble(3.0))
                    )
                }
                .renderer(SimpleRenderer())
                .build()
            emptyScene.world.addGameObject(go)
        }
        return emptyScene
    }
}