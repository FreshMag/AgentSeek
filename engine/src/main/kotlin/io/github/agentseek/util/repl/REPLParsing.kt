package io.github.agentseek.util.repl

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.Rays.castRay
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.github.agentseek.util.repl.GameREPL.isRunning
import io.github.agentseek.util.repl.GameREPL.scene
import io.github.agentseek.view.Renderer
import picocli.CommandLine.*
import java.io.File
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit

/**
 * Object that contains methods and commands for parsing REPL commands.
 */
object REPLParsing {

    /**
     * Creates a component from its fully qualified class name and adds it to the GameObject.
     *
     * @param className The fully qualified name of the component class.
     * @return The created component, or null if the class was not found.
     */
    private fun GameObject.createComponentFromClass(className: String): Component? {
        try {
            val component: Component = ClassLoader.getSystemClassLoader()
                .loadClass(className)
                .getConstructor(GameObject::class.java)
                .newInstance(this) as Component
            return component
        } catch (e: ClassNotFoundException) {
            println("Component class $className not found")
        }
        return null
    }

    /**
     * Adds a component to the GameObject from its fully qualified class name.
     *
     * @param className The fully qualified name of the component class.
     */
    private fun GameObject.addComponentFromFQName(className: String) {
        createComponentFromClass(className)?.let {
            try {
                this.addComponent(it)
            } catch (e: IllegalStateException) {
                println("The GameObject already has that component")
            }
        }
    }

    /**
     * Removes a component from the GameObject by its fully qualified class name.
     *
     * @param className The fully qualified name of the component class.
     */
    private fun GameObject.removeComponentFromFQName(className: String) {
        val clazz = Class.forName(className)
        components.find { clazz.isInstance(it) }?.let { removeComponent(it) } ?: println(
            "This GameObject doesn't have Component of class $className"
        )
    }

    /**
     * Sets the renderer of the GameObject from its fully qualified class name.
     *
     * @param className The fully qualified name of the renderer class.
     */
    fun GameObject.setRendererFromFQName(className: String) {
        try {
            val renderer: Renderer<*> = ClassLoader.getSystemClassLoader()
                .loadClass(className)
                .getConstructor()
                .newInstance() as Renderer<*>
            this.renderer = renderer
        } catch (e: ClassNotFoundException) {
            println("Renderer class $className not found")
        }
    }

    @Command(
        name = "",
        subcommands = [
            HelpCommand::class,
            StartCommand::class,
            PauseCommand::class,
            ResumeCommand::class,
            ExitCommand::class,
            Do::class,
            SetScene::class,
            LoadGO::class,
            ListObjects::class,
            AddGO::class,
            InspectGO::class,
            ModifyGO::class,
            DeleteGO::class,
            WatchGO::class,
            UnWatchGo::class,
            ZoomCommand::class,
            RayCommand::class,
        ],
        description = ["Game Read-Eval-Print-Loop for utility"],
        version = [
            "@|yellow AgentSeek-REPL |@",
            "@|blue Build 1.0|@",
            "@|red,bg(white) (c) 2024|@"],
    )
    class REPLCommand : Runnable {
        /**
         * Runs the REPL command, displaying a help message.
         */
        override fun run() {
            println("Type 'help' to see available commands")
        }
    }

    @Command(
        name = "start",
        description = ["Starts an infinite game loop"]
    )
    class StartCommand : Runnable {
        /**
         * Starts the game engine and sets the running flag to true.
         */
        override fun run() {
            GameEngine.start()
            isRunning = true
        }
    }

    @Command(
        name = "pause",
        description = ["Pauses a running game loop"]
    )
    class PauseCommand : Runnable {
        /**
         * Pauses the game engine and sets the running flag to false.
         */
        override fun run() {
            GameEngine.pause()
            isRunning = false
        }
    }

    @Command(
        name = "resume",
        description = ["Resumes the game loop"]
    )
    class ResumeCommand : Runnable {
        /**
         * Resumes the game engine and sets the running flag to true.
         */
        override fun run() {
            GameEngine.resume()
            isRunning = true
        }
    }

    @Command(
        name = "exit",
        description = ["Exits the program"]
    )
    class ExitCommand : Runnable {
        /**
         * Stops the game engine and exits the program.
         */
        override fun run() {
            GameEngine.stop()
            exitProcess(0)
        }
    }

    @Command(
        name = "addgo",
        description = ["Adds a game object"],
        subcommands = [HelpCommand::class]
    )
    class AddGO : Runnable {
        @Option(
            names = ["-s", "--shape"],
            description = ["specify the form of the game object (e.g. circle(<RADIUS>), rectangle(<WIDTH>, <HEIGHT>)," +
                    "square(<SIZE>))"],
            required = false,
        )
        var form: String = "square(${GameObject.DEFAULT_SIZE})"

        @Option(
            names = ["-x"],
            description = ["specify the x coordinate"],
        )
        var x: Double = 0.0

        @Option(
            names = ["-y"],
            description = ["specify the y coordinate"],
        )
        var y: Double = 0.0

        @Option(
            names = ["-c", "--components"],
            split = ",",
            description = ["Fully qualified names of the Components to be added to this Game Object.\n" +
                    "Note: components classes *must* have only a GameObject as parameter in the constructor"],
            arity = "1..*"
        )
        var components: Array<String> = emptyArray()

        /**
         * Adds a new game object to the scene with the specified shape, position, and components.
         */
        override fun run() {
            val go = scene.emptyGameObject(false)
            components.forEach {
                go.addComponentFromFQName(it)
            }
            val hitBox = parseForm(form, go)
            go.rigidBody = hitBox ?: return
            go.position = Point2d(x, y)
            go.renderer = GameEngine.view?.defaultRenderer() ?: return
            scene.world.addGameObject(go.also { println(it.id) })
        }
    }

    /**
     * Parses the form string to create a RigidBody for the GameObject.
     *
     * @param form The form string (e.g., "circle(10)").
     * @param gameObject The GameObject to which the RigidBody will be added.
     * @return The created RigidBody, or null if the form string is invalid.
     */
    private fun parseForm(form: String, gameObject: GameObject): RigidBody? {
        try {
            val split = form.split("(")
            val shape = split[0]
            val args = split[1].substringBefore(")").split(",")
            return when (shape) {
                "circle" -> RigidBody.CircleRigidBody(gameObject, args[0].toDouble())
                "rectangle" -> RigidBody.RectangleRigidBody(gameObject, args[0].toDouble(), args[1].toDouble())
                "square" -> RigidBody.RectangleRigidBody(gameObject, args[0].toDouble(), args[0].toDouble())
                else -> throw IllegalArgumentException()
            }
        } catch (e: NumberFormatException) {
            println("Please provide an integer value between parentheses")
            return null
        } catch (e: Exception) {
            println("$form is not a valid form expression. Try circle(<RADIUS>) or rectangle(<WIDTH>, <HEIGHT>)")
            return null
        }
    }

    @Command(
        name = "delgo",
        description = ["Deletes a gameobject"],
        subcommands = [HelpCommand::class]
    )
    class DeleteGO : Runnable {
        @Parameters(
            description = ["ID of the game object to delete"],
        )
        lateinit var id: String

        /**
         * Deletes the game object with the specified ID from the scene.
         */
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                scene.world.removeGameObject(it)
                println("Removed $id")
            } ?: println("ID doesn't match any GameObject")
        }
    }

    @Command(
        name = "inspect",
        description = ["Inspects a game object"],
        subcommands = [HelpCommand::class]
    )
    class InspectGO : Runnable {
        @Parameters(
            description = ["ID of the game object to inspect"],
        )
        lateinit var id: String

        /**
         * Inspects the game object with the specified ID and prints its details.
         */
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                println(it.toString())
            } ?: println("ID doesn't match any GameObject")
        }
    }

    @Command(
        name = "modify",
        description = ["Modifies a game object"],
        subcommands = [HelpCommand::class],
        sortOptions = false
    )
    class ModifyGO : Runnable {
        @Parameters(
            description = ["ID of the game object to modify"],
        )
        lateinit var id: String

        @Option(
            names = ["add", "--component"],
            description = ["Fully qualified name of the Component to be added to this Game Object"],
        )
        var componentFQName: String = ""

        @Option(
            names = ["renderer", "--with-renderer"],
            description = ["Fully qualified name of the Renderer class to be added to this Game Object"],
        )
        var rendererFQName: String = ""

        @Option(
            names = ["remove", "--remove-component"],
            description = ["Fully qualified name of the Component class to be removed from the Game Object"],
        )
        var removedComponentFQName: String = ""

        @Option(
            names = ["-x"],
            description = ["specify the x coordinate"],
        )
        var x: Double = Double.MAX_VALUE

        @Option(
            names = ["-y"],
            description = ["specify the y coordinate"],
        )
        var y: Double = Double.MAX_VALUE

        /**
         * Modifies the game object with the specified ID by adding/removing components, setting the renderer, or
         * changing its position.
         */
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                if (componentFQName.isNotBlank()) {
                    it.addComponentFromFQName(componentFQName)
                }
                if (rendererFQName.isNotBlank()) {
                    it.setRendererFromFQName(rendererFQName)
                }
                if (removedComponentFQName.isNotBlank()) {
                    it.removeComponentFromFQName(removedComponentFQName)
                }
                if (x != Double.MAX_VALUE) {
                    it.position = Point2d(x, it.position.y)
                }
                if (y != Double.MAX_VALUE) {
                    it.position = Point2d(it.position.x, y)
                }
                println(it.toString())
            } ?: println("ID doesn't match any GameObject")

        }
    }

    @Command(
        name = "listobj",
        description = ["Lists all the game objects"],
        subcommands = [HelpCommand::class],
    )
    class ListObjects : Runnable {

        @Option(
            names = ["-v", "--verbose"],
            description = ["Prints all the information of each GameObject, not just the ID"],
        )
        var verbose: Boolean = false

        /**
         * Lists all game objects in the scene, optionally printing detailed information.
         */
        override fun run() {
            scene.world.gameObjects.forEach {
                println(if (verbose) it.toString() else it.id)
            }
        }

    }

    @Command(
        name = "watch",
        description = ["Watches a GameObject, showing GameObject information at each game loop"],
        subcommands = [HelpCommand::class],
    )
    class WatchGO : Runnable {
        @Parameters(
            description = ["ID of the game object to watch"],
        )
        lateinit var id: String

        /**
         * Adds a WatchComponent to the game object with the specified ID to monitor its state.
         */
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                it.addComponent(WatchComponent(it))
                println("Begun watching GameObject with ID: $id")
            } ?: println("ID doesn't match any GameObject")
        }
    }

    @Command(
        name = "unwatch",
        description = ["Un-watches a previously watched GameObject"],
        subcommands = [HelpCommand::class],
    )
    class UnWatchGo : Runnable {
        @Parameters(
            description = ["ID of the game object to un-watch"],
        )
        lateinit var id: String

        /**
         * Removes the WatchComponent from the game object with the specified ID.
         */
        override fun run() {
            scene.world.gameObjectById(id)?.let {
                if (it.hasComponent<WatchComponent>()) {
                    it.removeComponent(it.getComponent<WatchComponent>()!!)
                    println("Stopped watching GameObject with ID: $id")
                } else {
                    println("Wasn't watching GameObject with ID: $id")
                }
            } ?: println("ID doesn't match any GameObject")
        }
    }

    @Command(
        name = "scene",
        description = ["Sets the scene taken from a file"],
        subcommands = [HelpCommand::class],
    )
    class SetScene : Runnable {

        @Parameters(paramLabel = "SCENE_FILE", description = ["File containing the scene to set"])
        lateinit var file: File

        /**
         * Sets the scene from the specified file.
         */
        override fun run() {
            TODO()
        }
    }

    @Command(
        name = "do",
        description = ["Does a number of iterations of the game loop"],
        subcommands = [HelpCommand::class],
    )
    class Do : Runnable {
        @Parameters(paramLabel = "N_ITERATIONS", description = ["Number of iterations of the game loop"])
        var nIterations: Int = 0

        @Option(
            names = ["-dt", "--delta-time"],
            description = ["Defines the artificial delta time of these iterations"]
        )
        var deltaTime: Long = 100.milliseconds.toLong(DurationUnit.MILLISECONDS)

        /**
         * Executes the specified number of iterations of the game loop with the given delta time.
         */
        override fun run() {
            repeat((1..nIterations).count()) {
                GameEngine.doOne(deltaTime.milliseconds)
                Thread.sleep(deltaTime)
            }
        }
    }

    @Command(
        name = "loadgo",
        description = ["Loads a game object from a file"],
        subcommands = [HelpCommand::class],
    )
    class LoadGO : Runnable {
        @Parameters(
            paramLabel = "GAME_OBJECT_FILE", description = ["File containing GameObject specification"]
        )
        lateinit var file: File

        /**
         * Loads a game object from the specified file.
         */
        override fun run() {
            TODO("Not yet implemented")
        }
    }

    @Command(
        name = "zoom",
        description = ["Zooms the viewport of the view's camera"],
        subcommands = [HelpCommand::class],
    )
    class ZoomCommand : Runnable {
        @Parameters(
            paramLabel = "FACTOR", description = ["Zoom's factor. MUST be positive. If less than 1, zooms out"]
        )
        var factor: Double = 1.0

        /**
         * Zooms the camera by the specified factor.
         */
        override fun run() {
            GameEngine.view?.camera?.zoom(factor.takeIf { it > 0 } ?: 1.0)
        }
    }

    @Command(
        name = "ray",
        description = ["Casts a ray between two GameObjects and prints the intersected game objects, sorted by distance"],
        subcommands = [HelpCommand::class],
    )
    class RayCommand : Runnable {
        @Parameters(
            paramLabel = "FIRST_OBJ", description = ["ID of the GameObject used as origin of the ray"]
        )
        var firstId: String = ""

        @Parameters(
            paramLabel = "SECOND_OBJ", description = ["ID of the GameObject used as destination of the ray"]
        )
        var secondId: String = ""

        /**
         * Casts a ray between two game objects and prints the IDs of the intersected game objects, sorted by distance.
         */
        override fun run() {
            scene.world.gameObjectById(firstId)?.let { gameObject ->
                val second = scene.world.gameObjectById(secondId) ?: return@let
                println(gameObject.castRay(second).allIntersecting.map { it.gameObject.id })
            }
        }
    }

}