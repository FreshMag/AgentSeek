package io.github.agentseek.util.factories

import io.github.agentseek.components.*
import io.github.agentseek.components.jason.CameraAgentComponent
import io.github.agentseek.components.jason.GuardAgentComponent
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
import io.github.agentseek.util.factories.GameObjects.cameraAgent
import io.github.agentseek.util.factories.GameObjects.door
import io.github.agentseek.util.factories.GameObjects.guardAgent
import io.github.agentseek.util.factories.GameObjects.player
import io.github.agentseek.util.factories.GameObjects.wall
import io.github.agentseek.util.factories.GameObjects.walls
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.jasonAgent
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
import io.github.agentseek.view.CameraRenderer
import io.github.agentseek.view.DoorRenderer
import io.github.agentseek.view.SimpleRenderer
import io.github.agentseek.view.gui.GameGui
import kotlin.math.PI

object Scenes {
    fun exampleScene(nObjects: Int): Scene = scene(
        gameObject(
            { InputComponent(it) },
            { NoiseEmitterComponent(it, 6.0) },
            { NoiseEmitterVisualComponent(it) },
            rigidBody = circle(0.5),
            renderer = default()
        ), *((0 until nObjects).flatMap { i ->
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

    fun collisionExampleScene(): Scene = scene(
        // Moving GameObject
        gameObject(
            { ConstantAccelerationComponent(it, vector(2, 2)) }, rigidBody = circle(5), renderer = default()
        ),
        // Cone to collide with
        gameObject(
            position = point(10, 10), rigidBody = cone(90, 7, degrees(PI / 7))
        )
    )

    fun jasonExampleScene(): Scene = sceneWithJason(
        name = "example", environmentClass = AgentSeekEnvironment::class,
        agents = agents(
            jasonAgent(
                id = "camera1",
                aslName = "camera_agent",
                agentComponent = { id, go -> CameraAgentComponent(go, id) },
                position = point(4, 4),
                renderer = CameraRenderer(),
            ), jasonAgent(
                id = "camera2",
                aslName = "camera_agent",
                agentComponent = { id, go -> CameraAgentComponent(go, id) },
                position = point(46, 4),
                renderer = CameraRenderer(),
            ), jasonAgent(
                id = "agent1",
                aslName = "guard_agent",
                agentComponent = { id, go -> GuardAgentComponent(go, id) },
                { NoiseSensorComponent(it, 3.0) },
                { SightSensorComponent(it, 7.0, 1.0) },
                { DistanceSensorComponent(it, 2.0) },
                { FieldMovementComponent(it) },
                position = point(10, 4),
                rigidBody = square(2.0),
                renderer = SimpleRenderer(),
            ), jasonAgent(
                id = "agent2",
                aslName = "guard_agent",
                agentComponent = { id, go -> GuardAgentComponent(go, id) },
                { NoiseSensorComponent(it, 3.0) },
                { SightSensorComponent(it, 6.0, 2.0) },
                { DistanceSensorComponent(it, 2.0) },
                { FieldMovementComponent(it) },
                position = point(40, 4),
                rigidBody = square(2.0),
                renderer = SimpleRenderer(),
            )
        ),
        gameObject(
            { NoiseEmitterComponent(it, 3.0) },
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
    )

    fun levelOnee(): Scene = sceneWithJason(
        name = "example", environmentClass = AgentSeekEnvironment::class,
        agents = agents(
            jasonAgent(
                id = "camera1",
                aslName = "camera_agent",
                agentComponent = { id, go -> CameraAgentComponent(go, id) },
                position = point(GameGui.camera.viewPortWidth - 4, GameGui.camera.viewPortHeight - 10),
                renderer = CameraRenderer(),
            ), jasonAgent(
                id = "agent1",
                aslName = "guard_agent",
                agentComponent = { id, go -> GuardAgentComponent(go, id) },
                { NoiseSensorComponent(it, 3.0) },
                { SightSensorComponent(it, 7.0, 1.0) },
                { DistanceSensorComponent(it, 2.0) },
                { FieldMovementComponent(it) },
                position = point(10, 4),
                rigidBody = square(2.0),
                renderer = SimpleRenderer(),
            )
        ),
        gameObject(
            { NoiseEmitterComponent(it, 3.0) },
            { InputComponent(it) },
            position = point(GameGui.camera.viewPortWidth - 5, GameGui.camera.viewPortHeight - 6.5),
            rigidBody = rectangle(2, 2),
            renderer = GameGui.defaultRenderer(),
            name = "Player"
        ),
        *bounds(2.5, GameGui.defaultRenderer(), GameGui.camera.viewPortWidth, GameGui.camera.viewPortHeight),
        gameObject(
            position = point(GameGui.camera.viewPortWidth - 15, GameGui.camera.viewPortHeight - 8),
            rigidBody = rectangle(15, 1).with(isStatic = true),
            name = "Wall",
            renderer = GameGui.defaultRenderer()
        ),
        gameObject(
            { DoorComponent(it, "jasonExample") },
            position = point(0, 10),
            rigidBody = square(2.5).with(isStatic = true),
            renderer = DoorRenderer(),
        ),
    )

    fun levelOne(): Scene = sceneWithJason(
        name = "example", environmentClass = AgentSeekEnvironment::class,
        agents = agents(
            cameraAgent(
                id = "camera1",
                position = point(GameGui.camera.viewPortWidth - 4, GameGui.camera.viewPortHeight - 10),
            ), guardAgent(
                id = "agent1",
                position = point(10, 4),
            )
        ),
        player(
            position = point(GameGui.camera.viewPortWidth - 5, GameGui.camera.viewPortHeight - 6.5)
        ),
        *bounds(2.5, GameGui.defaultRenderer(), GameGui.camera.viewPortWidth, GameGui.camera.viewPortHeight),
        *walls(
            wall(
                x = GameGui.camera.viewPortWidth - 15,
                y = GameGui.camera.viewPortHeight - 8,
                width = 15,
                height = 1,
            ),
            wall(
                x = 15,
                y = 5,
                width = 10,
                height = 1,
            ),
            wall(
                x = 8,
                y = 13,
                width = 20,
                height = 1,
            ),
            wall(
                x = 0,
                y = 23,
                width = 35,
                height = 1,
            ),
            wall(
                x = 8,
                y = 0,
                width = 1,
                height = 16,
            ),
            wall(
                x = 12,
                y = 18,
                width = 1,
                height = 5,
            ),
            wall(
                x = 22,
                y = 18,
                width = 1,
                height = 5,
            ),
            wall(
                x = 34,
                y = 13,
                width = 1,
                height = 10,
            ),
            wall(
                x = 40,
                y = 18,
                width = 1,
                height = 5,
            ),
            wall(
                x = 34,
                y = 14,
                width = 7,
                height = 1,
            ),
            wall(
                x = 25,
                y = 24,
                width = 1,
                height = 14,
            )
        ),
        door(
            "jasonExample",
            x = 0,
            y = 10
        )
    )
}