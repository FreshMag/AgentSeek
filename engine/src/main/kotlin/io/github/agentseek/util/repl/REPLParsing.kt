package io.github.agentseek.util.repl

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.RigidBody
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.github.agentseek.util.repl.GameREPL.isRunning
import io.github.agentseek.util.repl.GameREPL.scene
import io.github.agentseek.view.Renderer
import io.github.agentseek.view.swing.SimpleRenderer
import picocli.CommandLine.*
import java.io.File
import kotlin.system.exitProcess
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit


object REPLParsing {

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

    private fun GameObject.addComponentFromFQName(className: String) {
        createComponentFromClass(className)?.let {
            try {
                this.addComponent(it)
            } catch (e: IllegalStateException) {
                println("The GameObject already has that component")
            }
        }
    }

    private fun GameObject.removeComponentFromFQName(className: String) {
        val clazz = Class.forName(className)
        components.find { clazz.isInstance(it) }?.let { removeComponent(it) } ?: println(
            "This GameObject doesn't have Component of class $className"
        )
    }

    fun GameObject.setRendererFromFQName(className: String) {
        try {
            val renderer: Renderer = ClassLoader.getSystemClassLoader()
                .loadClass(className)
                .getConstructor()
                .newInstance() as Renderer
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
        ],
        description = ["Game Read-Eval-Print-Loop for utility"],
        version = [
            "@|yellow AgentSeek-REPL |@",
            "@|blue Build 1.0|@",
            "@|red,bg(white) (c) 2024|@"],
    )
    class REPLCommand : Runnable {
        override fun run() {
            println("Type 'help' to see available commands")
        }
    }

    @Command(
        name = "start",
        description = ["Starts an infinite game loop"]
    )
    class StartCommand : Runnable {
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

        override fun run() {
            val go = scene.emptyGameObject(false)
            components.forEach {
                go.addComponentFromFQName(it)
            }
            val hitBox = parseForm(form, go)
            go.rigidBody = hitBox ?: return
            go.position = Point2d(x, y)
            go.renderer = SimpleRenderer()
            scene.world.addGameObject(go.also { println(it.id) })
        }
    }

    private fun parseForm(form: String, gameObject: GameObject): RigidBody? {
        try {
            val split = form.split("(")
            val shape = split[0]
            val args = split[1].substringBefore(")").split(",")
            return when (shape) {
                "circle" -> RigidBody.CircleRigidBody(args[0].toDouble(), gameObject)
                "rectangle" -> RigidBody.RectangleRigidBody(args[0].toDouble(), args[1].toDouble(), gameObject)
                "square" -> RigidBody.RectangleRigidBody(args[0].toDouble(), args[0].toDouble(), gameObject)
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

        override fun run() {
            TODO("Not yet implemented")
        }
    }

}