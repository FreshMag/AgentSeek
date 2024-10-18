package io.github.agentseek.util.repl

import io.github.agentseek.common.Point2d
import io.github.agentseek.components.Component
import io.github.agentseek.core.GameObject
import io.github.agentseek.core.engine.GameEngine
import io.github.agentseek.physics.CircleHitBox
import io.github.agentseek.physics.HitBox
import io.github.agentseek.util.FastEntities.emptyGameObject
import io.github.agentseek.util.repl.GameREPL.isRunning
import io.github.agentseek.util.repl.GameREPL.scene
import io.github.agentseek.view.Renderer
import io.github.agentseek.view.SimpleRenderer
import picocli.CommandLine.*
import kotlin.system.exitProcess

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
            ListObjects::class,
            AddGO::class,
            InspectGO::class,
            ModifyGO::class,
            DeleteGO::class],
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
        private val go = scene.emptyGameObject(false)

        @Option(
            names = ["-f", "--form"],
            description = ["specify the form of the game object (e.g. circle(<RADIUS>), rectangle(<WIDTH>, <HEIGHT>))"],
            required = false,
        )
        var form: String = "circle(${GameObject.DEFAULT_HITBOX_RADIUS})"

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
            components.forEach {
                go.addComponentFromFQName(it)
            }
            val hitBox = parseForm(form)
            go.hitBox = hitBox ?: return
            go.position = Point2d(x, y)
            go.renderer = SimpleRenderer()
            scene.world.addGameObject(go.also { println(it.id) })
        }
    }

    private fun parseForm(form: String): HitBox? {
        try {
            val split = form.split("(")
            val shape = split[0]
            val args = split[1].substringBefore(")").split(",")
            return when (shape) {
                "circle" -> CircleHitBox(args[0].toInt())
                else -> CircleHitBox(GameObject.DEFAULT_HITBOX_RADIUS)
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
    class ModifyGO: Runnable {
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
    class ListObjects: Runnable {

        @Option(
            names = ["-v", "--verbose"],
            description = ["Prints all the information of each GameObject, not just the ID"],
        )
        var verbose: Boolean = false

        override fun run() {
            scene.world.gameObjects.forEach {
                println(if(verbose) it.toString() else it.id)
            }
        }

    }
}