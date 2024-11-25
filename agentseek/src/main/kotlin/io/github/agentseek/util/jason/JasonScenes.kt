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

/**
 * Helper functions to create scenes with Jason agents.
 */
object JasonScenes {

    /**
     * Configuration for a Jason agent.
     * @param asl The agent's ASL name.
     * @param gameObjectSetter A function that creates a GameObject for the agent.
     */
    data class JasonAgentConfig(
        val asl: Agent,
        val gameObjectSetter: (World) -> GameObject
    )

    /**
     * Creates a Jason agent configuration.
     * @param id The agent's ID.
     * @param aslName The agent's ASL name.
     * @param agentComponent A function that creates the agent's JasonAgent component.
     * @param componentsSetters Functions that create other components for the agent.
     * @param rigidBody A function that creates the agent's RigidBody.
     * @param renderer The agent's renderer.
     * @param position The agent's initial position.
     * @return The Jason agent configuration.
     */
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

    /**
     * Creates a list of Jason agent configurations.
     */
    fun agents(
        vararg agents: JasonAgentConfig
    ): List<JasonAgentConfig> =
        agents.toList()

    /**
     * Creates a scene with Jason agents.
     * @param name The scene's name.
     * @param environmentClass The environment class.
     * @param agents The Jason agents.
     * @param otherGameObjects Other game objects to add to the scene.
     */
    fun sceneWithJason(
        name: String,
        environmentClass: KClass<out Environment>,
        agents: List<JasonAgentConfig>,
        vararg otherGameObjects: (World) -> GameObject,
        hideJasonGui: Boolean = false,
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