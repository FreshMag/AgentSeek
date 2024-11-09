package io.github.agentseek.util.factories

import io.github.agentseek.components.*
import io.github.agentseek.components.jason.GuardAgentComponent
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
import io.github.agentseek.util.jason.JasonScenes.agents
import io.github.agentseek.util.jason.JasonScenes.jasonAgent
import io.github.agentseek.util.jason.JasonScenes.sceneWithJason
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
        name = "example",
        environmentClass = AgentSeekEnvironment::class,
        agents = agents(
            jasonAgent(
                id = "agent1",
                aslName = "guard_agent",
                agentComponent = { id, go -> GuardAgentComponent(go, id) },
                { SightSensorComponent(it, 7.0, 1.0) },
                position = point(0, 0),
                rigidBody = square(2.0),
                renderer = GameGui.defaultRenderer(),
            )
        ),

        gameObject(
            { InputComponent(it) },
            name = "Player",
            position = point(5, 5),
            rigidBody = rectangle(2, 2),
            renderer = GameGui.defaultRenderer()
        ),
        /*      gameObject(
                  { NoiseSensorComponent(it, 3.0) },
                  { NoiseSensorVisualComponent(it) },
                  position = point(15, 15),
                  rigidBody = rectangle(4, 4).with(isStatic = true),
                  renderer = GameGui.defaultRenderer()
              )*/
    )

}