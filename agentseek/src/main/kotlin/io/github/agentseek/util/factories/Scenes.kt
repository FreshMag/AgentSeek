package io.github.agentseek.util.factories

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.*
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.view.SimpleRenderer
import io.github.agentseek.view.gui.GameGui

object Scenes {

    fun exampleScene(nObjects: Int): Scene {
        val emptyScene = emptyScene()
        val maxX = GameEngine.view?.camera?.viewPortWidth ?: 50.0
        val maxY = GameEngine.view?.camera?.viewPortHeight ?: 50.0
//        val player =
//            emptyScene.world.gameObjectBuilder().position(10.0, 10.0)
//                .with { InputComponent(it) }
//                .with { SightSensorComponent(it, 10.0, Math.PI / 3) }
//                .renderer(SimpleRenderer()).build()
//        emptyScene.world.addGameObject(player)

        val agent =
            emptyScene.world.gameObjectBuilder().position(0.0, 0.0)
                .rigidBody { it ->
                    RigidBody.CircleRigidBody(0.5, it)
                }
//                .with { NoiseEmitterComponent(it, 3.0) }
                .with { InputComponent(it) }
                //.with { DistanceSensorComponent(it, 2.0) }
                //.with { FieldMovementComponent(it) }
                //.with { TestMouseComponent(it) }
                .with { NoiseEmitterComponent(it, 3.0) }
                .renderer(SimpleRenderer()).build()

        emptyScene.world.addGameObject(agent)
        (0 until nObjects).forEach { i ->
            (0 until nObjects).forEach { j ->
                val go =
                    emptyScene.world.gameObjectBuilder()
                        .position(5.0 + i * 5.0, 5.0 + j * 5.0)
                        .rigidBody {
                            RigidBody.RectangleRigidBody(2.0, 2.0, it)
                                .also { body -> body.isStatic = true }
                        }
                        .with { NoiseSensorComponent(it, 3.0) }
                        .with { NoiseSensorVisualComponent(it) }
                        .renderer(SimpleRenderer())
                        .build()

                emptyScene.world.addGameObject(go)

            }
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
            .rigidBody { RigidBody.ConeRigidBody(Math.PI / 2, 7.0, Math.PI / 7, it) }
            .renderer(GameGui.defaultRenderer())
            .build()
        emptyScene.world.addGameObject(toCollideGO)
        emptyScene.world.addGameObject(movingGO)
        return emptyScene
    }
}