package io.github.agentseek.util.factories

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.*
import io.github.agentseek.components.common.Config
import io.github.agentseek.components.jason.CameraAgentComponent
import io.github.agentseek.components.jason.GuardAgentComponent
import io.github.agentseek.components.jason.HearingAgentComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.FastEntities.circle
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.rectangle
import io.github.agentseek.util.FastEntities.square
import io.github.agentseek.util.FastEntities.with
import io.github.agentseek.util.factories.GameObjects.walls
import io.github.agentseek.util.jason.JasonScenes
import io.github.agentseek.util.jason.JasonScenes.jasonAgent
import io.github.agentseek.view.CameraRenderer
import io.github.agentseek.view.DoorRenderer
import io.github.agentseek.view.GuardRenderer
import io.github.agentseek.view.HearingRenderer
import io.github.agentseek.view.gui.GameGui
import io.github.agentseek.world.World

object GameObjects {

    /**
     * Creates a player game object.
     *
     * @param position The initial position of the player.
     * @param noiseEmittingRadius The radius within which the player emits noise.
     * @param rigidBody A function to create the player's rigid body.
     * @return A function that takes a World and returns a GameObject representing the player.
     */
    fun player(
        position: Point2d,
        noiseEmittingRadius: Number = Config.Player.noiseEmittingRadius,
        rigidBody: (GameObject) -> RigidBody = square(Config.Player.size),
        isCenter: Boolean = true,
    ): (World) -> GameObject =
        gameObject(
            { NoiseEmitterComponent(it, noiseEmittingRadius.toDouble()) },
            { MouseNoiseEmitterComponent(it) },
            { InputComponent(it) },
            position = position,
            rigidBody = rigidBody,
            renderer = GameGui.defaultRenderer(),
            name = Config.Player.name
        ).run {
            if (isCenter) {
                return@run { world: World ->
                    this(world).also { it.rigidBody.collider.center = position }
                }
            } else {
                return@run this
            }
        }

    /**
     * Creates a guard agent configuration.
     *
     * @param id The identifier for the guard agent.
     * @param position The initial position of the guard agent.
     * @param noiseSensorRadius The radius within which the guard agent can sense noise.
     * @param sightSensorConeLength The length of the sight sensor cone.
     * @param sightSensorConeAperture The aperture of the sight sensor cone.
     * @param distanceSensorRadius The radius within which the guard agent can sense distance.
     * @param rigidBody A function to create the guard agent's rigid body.
     * @return A JasonAgentConfig for the guard agent.
     */
    fun guardAgent(
        id: String,
        position: Point2d,
        noiseSensorRadius: Number = Config.Agents.guardDefaultNoiseSensorRadius,
        sightSensorConeLength: Number = Config.Agents.guardDefaultSightConeLength,
        sightSensorConeAperture: Number = Config.Agents.guardDefaultSightConeApertureRadians,
        distanceSensorRadius: Number = Config.Agents.guardDefaultDistanceSensorRadius,
        rigidBody: (GameObject) -> RigidBody = square(Config.Agents.guardSize),
        isCenter: Boolean = true,
    ): JasonScenes.JasonAgentConfig =
        jasonAgent(
            id = id,
            aslName = "guard_agent",
            agentComponent = { id, go -> GuardAgentComponent(go, id) },
            { NoiseSensorComponent(it, noiseSensorRadius.toDouble()) },
            {
                SightSensorComponent(
                    it,
                    sightSensorConeLength.toDouble(),
                    sightSensorConeAperture.toDouble(),
                    Config.Agents.cameraNamesToTrack.toSet()
                )
            },
            { DistanceSensorComponent(it, distanceSensorRadius.toDouble()) },
            { FieldMovementComponent(it, Config.Agents.guardMaxSpeed) },
            { GameOverComponent(it) },
            position = position,
            rigidBody = rigidBody,
            renderer = GuardRenderer(),
        ).apply {
            if (isCenter) {
                this.copy(
                    gameObjectSetter = { world ->
                        gameObjectSetter(world).also {
                            it.rigidBody.collider.center = position
                        }
                    }
                )
            }
        }

    fun hearingAgent(
        id: String,
        position: Point2d,
        noiseSensorRadius: Number = Config.Agents.hearingDefaultNoiseSensorRadius,
        distanceSensorRadius: Number = Config.Agents.guardDefaultDistanceSensorRadius,
        rigidBody: (GameObject) -> RigidBody = circle(Config.Agents.hearingSize),
        isCenter: Boolean = true,
    ): JasonScenes.JasonAgentConfig =
        jasonAgent(
            id = id,
            aslName = "hearing_agent",
            agentComponent = { id, go -> HearingAgentComponent(go, id) },
            { NoiseSensorComponent(it, noiseSensorRadius.toDouble()) },
            { DistanceSensorComponent(it, distanceSensorRadius.toDouble()) },
            { FieldMovementComponent(it, Config.Agents.hearingMaxSpeed) },
            { GameOverComponent(it) },
            position = position,
            rigidBody = rigidBody,
            renderer = HearingRenderer(),
        ).apply {
            if (isCenter) {
                this.copy(
                    gameObjectSetter = { world ->
                        gameObjectSetter(world).also {
                            it.rigidBody.collider.center = position
                        }
                    }
                )
            }
        }

    /**
     * Creates a camera agent configuration.
     *
     * @param id The identifier for the camera agent.
     * @param position The initial position of the camera agent.
     * @return A JasonAgentConfig for the camera agent.
     */
    fun cameraAgent(
        id: String,
        position: Point2d,
        isCenter: Boolean = true,
    ): JasonScenes.JasonAgentConfig =
        jasonAgent(
            id = id,
            aslName = "camera_agent",
            agentComponent = { id, go -> CameraAgentComponent(go, id) },
            position = position,
            renderer = CameraRenderer(),
        ).apply {
            if (isCenter) {
                this.copy(
                    gameObjectSetter = { world ->
                        gameObjectSetter(world).also {
                            it.rigidBody.collider.center = position
                        }
                    }
                )
            }
        }

    /**
     * Creates a wall game object.
     *
     * @param position position of the wall
     * @param width The width of the wall.
     * @param height The height of the wall.
     * @param name The name of the wall.
     * @return A function that takes a World and returns a GameObject representing the wall.
     */
    fun wall(
        position: Point2d,
        width: Number,
        height: Number,
        name: String = Config.Names.wallName,
        isCenter: Boolean = true,
    ): (World) -> GameObject =
        gameObject(
            position = position,
            rigidBody = rectangle(width, height).with(isStatic = true),
            name = name,
            renderer = GameGui.defaultRenderer()
        ).run {
            if (isCenter) {
                return@run { world: World ->
                    this(world).also { it.rigidBody.collider.center = position }
                }
            } else {
                return@run this
            }
        }

    fun wall(
        x: Number,
        y: Number,
        width: Number,
        height: Number,
        name: String = Config.Names.wallName,
        isCenter: Boolean = true,
    ): (World) -> GameObject =
        wall(point(x, y), width, height, name, isCenter)

    /**
     * Creates an array of wall game objects.
     *
     * @param walls Vararg of functions that take a World and return a GameObject representing a wall.
     * @return An array of functions that take a World and return a GameObject representing a wall.
     */
    fun walls(
        vararg walls: (World) -> GameObject,
    ): Array<(World) -> GameObject> =
        walls.withIndex().map { (index, go) ->
            { world: World -> go(world).also { it.name = "Wall$index" } }
        }.toTypedArray()

    /**
     * Creates a door game object.
     *
     * @param destinationSceneName The name of the destination scene.
     * @param position position of the door
     * @param size The size of the door.
     * @param name The name of the door.
     * @return A function that takes a World and returns a GameObject representing the door.
     */
    fun door(
        destinationSceneName: String,
        position: Point2d,
        size: Number = 2.5,
        name: String = Config.Names.doorName,
        isCenter: Boolean = true,
    ): (World) -> GameObject =
        gameObject(
            { DoorComponent(it, destinationSceneName) },
            position = position,
            rigidBody = square(size).with(isStatic = true),
            name = name,
            renderer = DoorRenderer()
        ).run {
            if (isCenter) {
                return@run { world: World ->
                    this(world).also { it.rigidBody.collider.center = position }
                }
            } else {
                return@run this
            }
        }

}
