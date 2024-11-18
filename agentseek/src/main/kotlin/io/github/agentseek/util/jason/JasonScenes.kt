package io.github.agentseek.util.jason

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.components.jason.Agent
import io.github.agentseek.components.jason.JasonAgent
import io.github.agentseek.components.jason.JasonInitializerComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.FastEntities.default
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.scene
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World
import jason.environment.Environment
import kotlin.reflect.KClass

object JasonScenes {

    data class JasonAgentConfig(
        val asl: Agent,
        val gameObjectSetter: (World) -> GameObject
    )

    fun jasonAgent(
        id: String,
        aslName: String,
        agentComponent: (String, GameObject) -> JasonAgent,
        vararg componentsSetters: (GameObject) -> Component,
        rigidBody: (GameObject) -> RigidBody = { go -> RigidBody.EmptyRigidBody(go) },
        renderer: Renderer<*> = default(),
        position: Point2d = point(0, 0)
    ): JasonAgentConfig =
        JasonAgentConfig(
            Agent(id, aslName),
            gameObject(
                { agentComponent(id, it) },
                *componentsSetters,
                rigidBody = rigidBody,
                renderer = renderer,
                position = position,
                name = id
            )
        )


    fun agents(
        vararg agents: JasonAgentConfig
    ): List<JasonAgentConfig> =
        agents.toList()


    fun sceneWithJason(
        name: String,
        environmentClass: KClass<out Environment>,
        agents: List<JasonAgentConfig>,
        hideJasonGui: Boolean = false,
        vararg otherGameObjects: (World) -> GameObject
    ): Scene =
        scene(
            // Jason manager
            gameObject({
                JasonInitializerComponent(
                    it,
                    name,
                    environmentClass.qualifiedName!!,
                    agents.map { it.asl }.toList(),
                    hideJasonGui,
                )
            }),
            *(agents.map { it.gameObjectSetter } + otherGameObjects).toTypedArray()
        )
}