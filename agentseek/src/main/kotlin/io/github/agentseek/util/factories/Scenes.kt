package io.github.agentseek.util.factories

import io.github.agentseek.components.*
import io.github.agentseek.components.jason.Agent
import io.github.agentseek.components.jason.BasicAgentComponent
import io.github.agentseek.components.jason.JasonInitializerComponent
import io.github.agentseek.core.Scene
import io.github.agentseek.env.AgentSeekEnvironment
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
            *((0 until nObjects).flatMap {
                i -> (0 until nObjects).map {
                    j ->
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
                renderer = default()
            ),
            // Cone to collide with
            gameObject(
                position = point(10, 10),
                rigidBody = cone(90, 7, degrees(PI / 7))
            )
        )

    fun jasonExampleScene(): Scene =
        scene(
            // Jason Manager
            gameObject({
                JasonInitializerComponent(
                    it,
                    "Example",
                    AgentSeekEnvironment::class.qualifiedName!!,
                    listOf(
                        Agent("agent1", "hello_agent"),
                        Agent("agent2", "hello_agent")
                    )
                )
            }),
            // Agent 1
            gameObject(
                { BasicAgentComponent(it, "agent1") },
                { InputComponent(it) },
                position = point(0, 0),
                rigidBody = square(3.0),
                renderer = default(),
                name = "Agent 1"
            ),
            // Agent 2
            gameObject(
                { BasicAgentComponent(it, "agent2") },
                { InputComponent(it) },
                position = point(4, 4),
                rigidBody = square(3.0),
                renderer = default(),
                name = "Agent 2"
            ),
        )
}