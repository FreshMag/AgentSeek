package io.github.agentseek.util.factories

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.ConstantAccelerationComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.view.gui.GameGui
import kotlin.random.Random

object Scenes {

    fun exampleScene(nObjects: Int): Scene {
        val emptyScene = emptyScene()
        val maxX = GameEngine.view?.camera?.viewPortWidth ?: 50.0
        val maxY = GameEngine.view?.camera?.viewPortHeight ?: 50.0
        (0 until nObjects).forEach { _ ->
            val go = emptyScene.world.gameObjectBuilder()
                .position(Random.nextDouble(0.0, maxX), Random.nextDouble(0.0, maxY))
                .with {
                    ConstantAccelerationComponent(
                        it,
                        Vector2d(Random.nextDouble(3.0), Random.nextDouble(3.0))
                    )
                }
                .rigidBody { RigidBody.ConeRigidBody(Math.PI / 4, 5.0, Math.PI / 2, it) }
                .renderer(GameGui.defaultRenderer())
                .build()
            emptyScene.world.addGameObject(go)
        }
        return emptyScene
    }

    fun collisionExampleScene(): Scene {
        val emptyScene = emptyScene()
        val movingGO = emptyScene.world.gameObjectBuilder()
            .position(0.0, 0.0)
            .with { ConstantAccelerationComponent(it, Vector2d(2.0, 2.0)) }
            .rigidBody { RigidBody.CircleRigidBody(5.0, it) }
            .renderer(GameGui.defaultRenderer())
            .build()

        val toCollideGO = emptyScene.world.gameObjectBuilder()
            .position(10.0, 10.0)
            .rigidBody { RigidBody.ConeRigidBody(Math.PI / 2, 7.0,  Math.PI / 7, it) }
            .renderer(GameGui.defaultRenderer())
            .build()
        emptyScene.world.addGameObject(toCollideGO)
        emptyScene.world.addGameObject(movingGO)
        return emptyScene
    }
}