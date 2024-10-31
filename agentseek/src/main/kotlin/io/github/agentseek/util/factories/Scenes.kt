package io.github.agentseek.util.factories

import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.*
import io.github.agentseek.components.jason.BasicAgentComponent
import io.github.agentseek.components.jason.JasonInitializerComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.view.EmptyRenderer
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
                    RigidBody.CircleRigidBody(it, 0.5)
                }
//                .with { NoiseEmitterComponent(it, 3.0) }
                .with { InputComponent(it) }
                //.with { DistanceSensorComponent(it, 2.0) }
                //.with { FieldMovementComponent(it) }
                //.with { TestMouseComponent(it) }
                .with { NoiseEmitterComponent(it, 6.0) }
                .with { NoiseEmitterVisualComponent(it) }
                .renderer(SimpleRenderer()).build()

        emptyScene.world.addGameObject(agent)
        (0 until nObjects).forEach { i ->
            (0 until nObjects).forEach { j ->
                val go =
                    emptyScene.world.gameObjectBuilder()
                        .position(5.0 + i * 5.0, 5.0 + j * 5.0)
                        .rigidBody {
                            RigidBody.RectangleRigidBody(it, 2.0, 2.0)
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
            .rigidBody { RigidBody.CircleRigidBody(it, 5.0) }
            .renderer(GameGui.defaultRenderer())
            .build()

        val toCollideGO = emptyScene.world.gameObjectBuilder()
            .position(10.0, 10.0)
            .rigidBody { RigidBody.ConeRigidBody(it, Math.PI / 2, 7.0, Math.PI / 7) }
            .renderer(GameGui.defaultRenderer())
            .build()
        emptyScene.world.addGameObject(toCollideGO)
        emptyScene.world.addGameObject(movingGO)
        return emptyScene
    }

    fun jasonExampleScene(): Scene {
        val emptyScene = emptyScene()
        val mas2jPath = "/mas2j/agentseek.mas2j"
        // Manager
        emptyScene.world.gameObjectBuilder()
            .with { JasonInitializerComponent(it, mas2jPath) }
            .renderer(EmptyRenderer())
            .buildAndAddToWorld()
        // Agent 1
        emptyScene.world.gameObjectBuilder()
            .with { BasicAgentComponent(it, "agent1") }
            .position(0, 0)
            .buildAndAddToWorld()
        return emptyScene
    }
}