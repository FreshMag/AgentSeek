package io.github.agentseek.util.factories

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.DistanceSensorComponent
import io.github.agentseek.components.DoorComponent
import io.github.agentseek.components.FieldMovementComponent
import io.github.agentseek.components.InputComponent
import io.github.agentseek.components.MouseNoiseEmitterComponent
import io.github.agentseek.components.NoiseEmitterComponent
import io.github.agentseek.components.NoiseSensorComponent
import io.github.agentseek.components.SightSensorComponent
import io.github.agentseek.components.jason.CameraAgentComponent
import io.github.agentseek.components.jason.GuardAgentComponent
import io.github.agentseek.core.GameObject
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.FastEntities.gameObject
import io.github.agentseek.util.FastEntities.point
import io.github.agentseek.util.FastEntities.rectangle
import io.github.agentseek.util.FastEntities.square
import io.github.agentseek.util.FastEntities.with
import io.github.agentseek.util.jason.JasonScenes
import io.github.agentseek.util.jason.JasonScenes.jasonAgent
import io.github.agentseek.view.CameraRenderer
import io.github.agentseek.view.DoorRenderer
import io.github.agentseek.view.SimpleRenderer
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
        noiseEmittingRadius: Number = 3,
        rigidBody: (GameObject) -> RigidBody = square(1.5),
    ): (World) -> GameObject =
        gameObject(
            { NoiseEmitterComponent(it, noiseEmittingRadius.toDouble()) },
            { MouseNoiseEmitterComponent(it) },
            { InputComponent(it) },
            position = position,
            rigidBody = rigidBody,
            renderer = GameGui.defaultRenderer(),
            name = "Player"
        )

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
        noiseSensorRadius: Number = 4.0,
        sightSensorConeLength: Number = 10,
        sightSensorConeAperture: Number = 1.2,
        distanceSensorRadius: Number = 2.8,
        rigidBody: (GameObject) -> RigidBody = square(1.5),
    ): JasonScenes.JasonAgentConfig =
        jasonAgent(
            id = id,
            aslName = "guard_agent",
            agentComponent = { id, go -> GuardAgentComponent(go, id) },
            { NoiseSensorComponent(it, noiseSensorRadius.toDouble()) },
            { SightSensorComponent(it, sightSensorConeLength.toDouble(), sightSensorConeAperture.toDouble()) },
            { DistanceSensorComponent(it, distanceSensorRadius.toDouble()) },
            { FieldMovementComponent(it) },
            position = position,
            rigidBody = rigidBody,
            renderer = SimpleRenderer(),
        )

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
    ): JasonScenes.JasonAgentConfig =
        jasonAgent(
            id = id,
            aslName = "camera_agent",
            agentComponent = { id, go -> CameraAgentComponent(go, id) },
            position = position,
            renderer = CameraRenderer(),
        )

    /**
     * Creates a wall game object.
     *
     * @param x The x-coordinate of the wall.
     * @param y The y-coordinate of the wall.
     * @param width The width of the wall.
     * @param height The height of the wall.
     * @param name The name of the wall.
     * @return A function that takes a World and returns a GameObject representing the wall.
     */
    fun wall(
        x: Number,
        y: Number,
        width: Number,
        height: Number,
        name: String = "Wall",
    ): (World) -> GameObject =
        gameObject(
            position = point(x, y),
            rigidBody = rectangle(width, height).with(isStatic = true),
            name = name,
            renderer = GameGui.defaultRenderer()
        )

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
     * @param x The x-coordinate of the door.
     * @param y The y-coordinate of the door.
     * @param size The size of the door.
     * @param name The name of the door.
     * @return A function that takes a World and returns a GameObject representing the door.
     */
    fun door(
        destinationSceneName: String,
        x: Number,
        y: Number,
        size: Number = 2.5,
        name: String = "Door",
    ): (World) -> GameObject =
        gameObject(
            { DoorComponent(it, destinationSceneName) },
            position = point(x, y),
            rigidBody = square(size).with(isStatic = true),
            name = name,
            renderer = DoorRenderer()
        )

}
