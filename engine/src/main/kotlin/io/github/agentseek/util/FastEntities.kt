package io.github.agentseek.util

import io.github.agentseek.common.Point2d
import io.github.agentseek.common.Vector2d
import io.github.agentseek.components.AbstractComponent
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.Scene
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.factories.SceneFactory.emptyScene
import io.github.agentseek.view.EmptyRenderer
import io.github.agentseek.view.Renderer
import io.github.agentseek.world.World
import kotlin.time.Duration

/**
 * Provides a set of utility functions to quickly create game objects and components.
 */
object FastEntities {
    /**
     * Creates an empty `GameObject` in the scene.
     *
     * If [addToWorld] is true, the object is automatically added to the world.
     */
    fun Scene.emptyGameObject(addToWorld: Boolean = true): GameObject =
        world.gameObjectBuilder().build().also { if (addToWorld) world.addGameObject(it) }

    /**
     * Creates a `Component` function with customizable initialization, update, and removal functions.
     */
    fun component(
        initFun: AbstractComponent.() -> Unit = {},
        updateFun: AbstractComponent.(Duration) -> Unit = {},
        onRemovedFun: AbstractComponent.() -> Unit = {}
    ): (GameObject) -> Component =
        { go ->
            object : AbstractComponent(go) {
                override fun init() = this.initFun()
                override fun onUpdate(deltaTime: Duration) = this.updateFun(deltaTime)
                override fun onRemoved() = this.onRemovedFun()
            }
        }

    /**
     * Creates a `GameObject` builder function with customizable components, rigid body, renderer, position, and name.
     *
     * The resulting builder function takes a `World` parameter to finalize the object construction within a specific world.
     *
     * - `componentsSetters`: Vararg of component functions to set up the `GameObject`.
     * - `rigidBody`: A function that creates a `RigidBody` for the `GameObject`. Defaults to an empty rigid body.
     * - `renderer`: Specifies the renderer. Defaults to an `EmptyRenderer`.
     * - `position`: The initial position of the `GameObject`. Defaults to `(0, 0)`.
     * - `name`: The name of the `GameObject`. Defaults to an empty string.
     */
    fun gameObject(
        vararg componentsSetters: (GameObject) -> Component,
        rigidBody: (GameObject) -> RigidBody = { go -> RigidBody.EmptyRigidBody(go) },
        renderer: Renderer<*> = EmptyRenderer(),
        position: Point2d = Point2d(0, 0),
        name: String = ""
    ): ((World) -> GameObject) = { world ->
        world.gameObjectBuilder()
            .rigidBody(rigidBody)
            .renderer(renderer)
            .position(position)
            .run { componentsSetters.fold(this) { builder, setter -> builder.with(setter) } }
            .name(name)
            .build()
    }

    /**
     * Creates a `Scene` and populates it with `GameObject` builder functions.
     *
     * Each builder function in `goSetters` is called with the `World` context to create and add
     * a `GameObject` to the world.
     *
     * - `goSetters`: Vararg of functions that define and add game objects to the scene.
     */
    fun scene(vararg goSetters: (World) -> GameObject): Scene =
        emptyScene().also { scene ->
            goSetters.forEach { scene.world.addGameObject(it(scene.world)) }
        }

    /**
     * Creates a `Point2d` with the specified `x` and `y` coordinates.
     */
    fun point(x: Number, y: Number) = Point2d(x.toDouble(), y.toDouble())

    /**
     * Creates a `Vector2d` with the specified `x` and `y` components.
     */
    fun vector(x: Number, y: Number) = Vector2d(x.toDouble(), y.toDouble())

    /**
     * Creates a circular `RigidBody` with the specified radius.
     *
     * The resulting function accepts a `GameObject` and returns a `RigidBody.CircleRigidBody`.
     */
    fun circle(radius: Number): (GameObject) -> RigidBody = { RigidBody.CircleRigidBody(it, radius.toDouble()) }

    /**
     * Creates a rectangular `RigidBody` with the specified width and height.
     *
     * The resulting function accepts a `GameObject` and returns a `RigidBody.RectangleRigidBody`.
     */
    fun rectangle(width: Number, height: Number): (GameObject) -> RigidBody = {
        RigidBody.RectangleRigidBody(it, width.toDouble(), height.toDouble())
    }

    /**
     * Creates a square `RigidBody` with the specified side length.
     *
     * Equivalent to calling `rectangle(size, size)`.
     */
    fun square(size: Number) = rectangle(size, size)

    /**
     * Creates a conical `RigidBody` with the specified angle, length, and rotation.
     *
     * - `angleDegrees`: The angle of the cone in degrees.
     * - `length`: The length of the cone.
     * - `rotationDegrees`: The rotation of the cone in degrees.
     *
     * The resulting function accepts a `GameObject` and returns a `RigidBody.ConeRigidBody`.
     */
    fun cone(
        angleDegrees: Number,
        length: Number,
        rotationDegrees: Number,
    ): (GameObject) -> RigidBody = {
        RigidBody.ConeRigidBody(
            it,
            Math.toRadians(angleDegrees.toDouble()),
            length.toDouble(),
            Math.toRadians(rotationDegrees.toDouble())
        )
    }

    /**
     * Modifies a `RigidBody` function to set the mass and static state of the `RigidBody`.
     *
     * - `mass`: The mass of the rigid body. Defaults to `1.0`.
     * - `isStatic`: Whether the rigid body is static. Defaults to `false`.
     */
    fun ((GameObject) -> RigidBody).with(
        mass: Number = 1.0,
        isStatic: Boolean = false,
    ): (GameObject) -> RigidBody = { go ->
        this(go).also {
            it.isStatic = isStatic
            it.mass = mass.toDouble()
        }
    }

    /**
     * Retrieves the default `Renderer` from the game engine view.
     */
    fun default(): Renderer<*> = GameEngine.view?.defaultRenderer()!!

    /**
     * Converts a radian value to degrees.
     */
    fun degrees(radians: Number): Double = Math.toDegrees(radians.toDouble())

    /**
     * Converts a degree value to radians.
     */
    fun radians(degrees: Number): Double = Math.toRadians(degrees.toDouble())

    /**
     * Gets the width of the camera's viewport.
     *
     * Defaults to `50.0` if no camera is available.
     */
    val cameraWidth: Double
        get() = GameEngine.view?.camera?.viewPortWidth ?: 50.0

    /**
     * Gets the height of the camera's viewport.
     *
     * Defaults to `50.0` if no camera is available.
     */
    val cameraHeight: Double
        get() = GameEngine.view?.camera?.viewPortHeight ?: 50.0


    /**
     * Returns an array of game object setters that adds rectangular bounds of size [size] to the world
     */
    fun bounds(
        size: Number,
        renderer: Renderer<*>,
        cameraWidth: Number,
        cameraHeight: Number,
        offCameraSize: Int = 50
    ): Array<(World) -> GameObject> =
        listOf(
            point(-offCameraSize + size.toDouble(), 0),
            point(cameraWidth.toDouble() - size.toDouble(), 0),
            point(0, cameraHeight.toDouble() - size.toDouble()),
            point(0, -offCameraSize + size.toDouble())
        ).map {
            gameObject(
                position = it,
                rigidBody = rectangle(offCameraSize, offCameraSize).with(isStatic = true),
                name = "Bounds",
                renderer = renderer
            )
        }.toTypedArray()

    /**
     * Returns a list of vectors corresponding to the four possible directions in this order: east, south, west, north.
     */
    fun allDirections(): Iterable<Vector2d> =
        listOf(
            vector(1, 0),
            vector(0, -1),
            vector(-1, 0),
            vector(0, 1),
        )

    /**
     * Returns a list of vectors corresponding to the eight possible directions in this order:
     * east, southeast, south, southwest, west, northwest, north, northeast.
     */
    fun allDirections8(): Iterable<Vector2d> =
        listOf(
            vector(1, 0),
            vector(1, -1),
            vector(0, -1),
            vector(-1, -1),
            vector(-1, 0),
            vector(-1, 1),
            vector(0, 1),
            vector(1, 1),
        )

}