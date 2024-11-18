package io.github.agentseek.util.factories

import io.github.agentseek.components.*
import io.github.agentseek.components.jason.CameraAgentComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
import io.github.agentseek.util.FastEntities.bounds
import io.github.agentseek.util.FastEntities.circle
import io.github.agentseek.util.FastEntities.cone
import io.github.agentseek.util.FastEntities.default
import io.github.agentseek.util.FastEntities.degrees
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.rectangle
import io.github.agentseek.util.FastEntities.scene
import io.github.agentseek.util.FastEntities.square
import io.github.agentseek.util.FastEntities.vector
import io.github.agentseek.util.FastEntities.with
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.jasonAgent
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
import io.github.agentseek.util.serialization.save
import io.github.agentseek.view.CameraRenderer
import io.github.agentseek.view.DoorRenderer
import io.github.agentseek.view.gui.GameGui
import kotlin.math.PI

object Scenes {
    fun exampleScene(nObjects: Int): Scene =
        scene(
            gameObject(
                { InputComponent(it) },
                { NoiseEmitterComponent(it, 6.0) },
                { NoiseEmitterVisualComponent(it) },
                rigidBody = circle(0.5),
                renderer = default()
            ),
            *((0 until nObjects).flatMap { i ->
                (0 until nObjects).map { j ->
                    gameObject(
                        { NoiseSensorComponent(it, 3.0) },
                        { NoiseSensorVisualComponent(it) },
                        position = point(5 + i * 5, 5 + j * 5),
                        rigidBody = rectangle(2, 2).with(isStatic = true),
                        renderer = default()
                    )
                }
            }).toTypedArray()
        )

    fun collisionExampleScene(): Scene =
        scene(
            // Moving GameObject
            gameObject(
                { ConstantAccelerationComponent(it, vector(2, 2)) },
                rigidBody = circle(5),
                renderer = GameGui.defaultRenderer()
            ),
            // Cone to collide with
            gameObject(
                position = point(10, 10),
                rigidBody = cone(90, 7, degrees(PI / 7))
            )
        )

    fun jasonExampleScene(): Scene =
        sceneWithJason(
            name = "example",
            environmentClass = AgentSeekEnvironment::class,
            agents = agents(
                jasonAgent(
                    id = "camera1",
                    aslName = "camera_agent",
                    agentComponent = { id, go -> CameraAgentComponent(go, id) },
                    position = point(4, 4),
                    renderer = CameraRenderer(),
                ),
                jasonAgent(
                    id = "camera2",
                    aslName = "camera_agent",
                    agentComponent = { id, go -> CameraAgentComponent(go, id) },
                    position = point(46, 4),
                    renderer = CameraRenderer(),
                ),
            ),
            hideJasonGui = true,
            gameObject(
                { NoiseSensorComponent(it, 3.0) },
                { NoiseSensorVisualComponent(it) },
                { InputComponent(it) },
                position = point(10, 10),
                rigidBody = rectangle(2, 2),
                renderer = GameGui.defaultRenderer(),
                name = "Player"
            ),
            gameObject(
                { NoiseSensorComponent(it, 3.0) },
                { NoiseSensorVisualComponent(it) },
                position = point(15, 15),
                rigidBody = rectangle(4, 4).with(isStatic = true),
                renderer = GameGui.defaultRenderer(),
            ),
            *bounds(2.5, GameGui.defaultRenderer(), GameGui.camera.viewPortWidth, GameGui.camera.viewPortHeight),
            gameObject(
                { DoorComponent(it, "jasonExample") },
                position = point(0, 10),
                rigidBody = square(2.5).with(isStatic = true),
                renderer = DoorRenderer(),
            ),
        ).also { it.save("./", "jasonExample") }
}